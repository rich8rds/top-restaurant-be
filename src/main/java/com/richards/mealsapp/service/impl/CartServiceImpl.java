package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.dto.CartResponse;
import com.richards.mealsapp.entity.*;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.exceptions.AlreadyExistsException;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.repository.*;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.AuthService;
import com.richards.mealsapp.service.CartService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    private final AuthService authService;
    private final PersonRepository personRepository;

    @Override
    public BaseResponse<String> addItemToCart(Long productId, String anonymousId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product does not exist"));

        Customer customer;
        String loggedInUser = UserUtil.extractEmailFromPrincipal().orElse(anonymousId);
        if(anonymousId.length() > 1) {
            log.info("ANONYMOUS_UUID: {}", anonymousId);
            customer = authService.registerAnonymousUser(anonymousId);
        }
        else {
            Person person = personRepository.findByEmail(loggedInUser)
                    .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
            customer = person.getCustomer();
        }

        Cart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();

        if(product.getAvailableQty() == 0)
            throw new ResourceNotFoundException("Product is out of stock!");

        for(CartItem cartItem : cartItems) {
            if(cartItem.getProduct().getId().equals(productId))
                return new BaseResponse<>(ResponseCodeEnum.ID_ALREADY_EXISTS, "Item already added to cart");
        }

        product.setAvailableQty(product.getAvailableQty() - 1);
        productRepository.save(product);

        CartItem cartItem = CartItem.builder()
                .cart(customer.getCart())
                .productName(product.getName())
                .imageUrl(product.getImageUrl())
                .unitPrice(BigDecimal.valueOf(product.getPrice()))
                .orderedQty(1)
                .product(product)
                .subTotal(BigDecimal.valueOf(product.getPrice()))
                .build();

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cartItems.add(savedCartItem);
        cartRepository.save(cart);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Item added to cart!");
    }

    @Override
    public BaseResponse<String> deleteItemFromCart(Long itemId, String anonymousId) {
        Customer customer = getCustomer(anonymousId);
        if(customer.getCart().getCartItems().isEmpty())
            return new BaseResponse<>(ResponseCodeEnum.ERROR, "Cart is Empty!");

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("Item is not in the cart"));

        customer.getCart().getCartItems().remove(cartItem);

        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new AlreadyExistsException("Product does not exist"));

        product.setAvailableQty(product.getAvailableQty() + cartItem.getOrderedQty());
        cartItemRepository.delete(cartItem);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Item successfully deleted");
    }

    @Override
    public BaseResponse<CartResponse> getCustomerCartItems(String anonymousId) {
        Customer customer = getCustomer(anonymousId);
        Cart cart = customer.getCart();
        List<CartItem> cartItems = new ArrayList<>(cart.getCartItems());
        if(cartItems.isEmpty())
            return new BaseResponse<>(ResponseCodeEnum.ERROR, "Cart is Empty!");

        double cartTotal = 0D;
        for(CartItem cartItem : cart.getCartItems()) {
            cartTotal += Double.parseDouble(""+cartItem.getSubTotal());
        }
        BigDecimal bdCartTotal = new BigDecimal(cartTotal);
        bdCartTotal = bdCartTotal.setScale(2, RoundingMode.HALF_UP);

        cartItems.sort(Comparator.comparing(CartItem::getId, Comparator.reverseOrder()));

        CartResponse cartResponse = CartResponse.builder()
                .cartItems(cartItems)
                .totalAmount(UserUtil.formatToLocale(bdCartTotal))
                .build();
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, cartResponse);
    }

    @Override
    public BaseResponse<String> addItemQuantityToCart(Long productId, String anonymousId) {
        Customer customer = getCustomer(anonymousId);
        Cart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();

        for(CartItem cartItem : cartItems) {
            if(cartItem.getProduct().getId().equals(productId)) {
                if(cartItem.getProduct().getAvailableQty() == 0)
                    throw new ResourceNotFoundException("Product is out of stock!");

                int newQty = cartItem.getOrderedQty() + 1;
                cartItem.setOrderedQty(newQty);
                double total = cartItem.getProduct().getPrice() * (newQty);
                cartItem.setSubTotal(BigDecimal.valueOf(total));
                cartItemRepository.save(cartItem);
            }
        }
//        cartRepository.save(cart);
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Item quantity increased");
    }

    @Override
    public BaseResponse<String> reduceCartItemQuantity(Long productId, String anonymousId) {
        Customer customer = getCustomer(anonymousId);
        Cart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();

        for(CartItem cartItem : cartItems) {
            if(cartItem.getProduct().getId().equals(productId)) {
                if(cartItem.getProduct().getAvailableQty() == 0)
                    throw new ResourceNotFoundException("Product is out of stock!");

                int newQty = cartItem.getOrderedQty() - 1;
                cartItem.setOrderedQty(newQty);
                double total = cartItem.getProduct().getPrice() * (newQty);
                cartItem.setSubTotal(BigDecimal.valueOf(total));
                cartItemRepository.save(cartItem);
            }
        }
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Item quantity increased");
    }

    @Override
    public BaseResponse<String> clearCart(String anonymousId) {
        Customer customer = getCustomer(anonymousId);
        Cart cart = customer.getCart();
        Set<CartItem> cartItems = cart.getCartItems();
        cartItems.clear();
        cart.setCartItems(cartItems);
        cart.setTotal(0.0);
        cartRepository.save(cart);
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Cart cleared successfully");
    }

    private Person getPerson(String email) {
        return personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist!"));
    }

    private Customer getCustomer(String anonymousId) {
        String loggedInUser = UserUtil.extractEmailFromPrincipal().orElse(anonymousId);
        if(anonymousId.length() > 1)
            return getPerson(anonymousId).getCustomer();
        return getPerson(loggedInUser).getCustomer();
    }
}
