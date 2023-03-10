package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.dto.OrderRequest;
import com.richards.mealsapp.entity.*;
import com.richards.mealsapp.enums.*;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.exceptions.UnauthorizedException;
import com.richards.mealsapp.repository.*;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.CartService;
import com.richards.mealsapp.service.OrderService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final TransactionRepository transactionRepository;
    private final CartService cartService;

    @Override
    public BaseResponse<String> checkoutOrder(OrderRequest orderRequest) {
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UnauthorizedException("You need to register or login to proceed."));

        Customer loggedInCustomer = customerRepository.findByPersonEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Cart cart = loggedInCustomer.getCart();


        BigDecimal bdTotal = BigDecimal.valueOf(cart.getTotal());
//                pickupCenter.getDeliveryFee().add(BigDecimal.valueOf(cart.getTotal()));

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Transaction transaction = Transaction.builder()
                .wallet(loggedInCustomer.getWallet())
                .amount(orderRequest.getGrandTotal().toString())
                .reference(UUID.randomUUID().toString())
                .purpose(PaymentPurpose.PURCHASE)
                .status(TransactionStatus.COMPLETED)
                .build();


        Order order = Order.builder()
                .grandTotal(bdTotal)
//                .deliveryFee(pickupCenter.getDeliveryFee())
                .address(address)
                .customer(loggedInCustomer)
                .transactionStatus(TransactionStatus.PENDING)
                .deliveryStatus(DeliveryStatus.YET_TO_ARRIVE)
                .modeOfDelivery(ModeOfDelivery.valueOf(orderRequest.getModeOfDelivery()))
                .build();

        Order savedOrder = orderRepository.save(order);

        cart.getCartItems().forEach(item -> {
            OrderItem orderItem = OrderItem.builder()
                    .orderedQty(item.getOrderedQty())
                    .order(savedOrder)
                    .productName(item.getProductName())
                    .subTotal(item.getSubTotal())
                    .unitPrice(item.getUnitPrice())
                    .product(item.getProduct())
                    .build();
            savedOrder.getOrderItems().add(orderItem);
        });

        transaction.setOrder(savedOrder);
        transactionRepository.save(transaction);
        cartService.clearCart("");
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Order made successfully!");
    }
}
