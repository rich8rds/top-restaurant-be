package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.dto.OrderRequest;
import com.richards.mealsapp.entity.*;
import com.richards.mealsapp.enums.DeliveryStatus;
import com.richards.mealsapp.enums.ModeOfDelivery;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.enums.TransactionStatus;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.exceptions.UnauthorizedException;
import com.richards.mealsapp.repository.CustomerRepository;
import com.richards.mealsapp.repository.OrderRepository;
import com.richards.mealsapp.repository.PickupCenterRepository;
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
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final PickupCenterRepository pickupCenterRepository;

    private final CartService cartService;

    @Override
    public BaseResponse<String> checkoutOrder(OrderRequest orderRequest) {
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UnauthorizedException("You need to register or login to proceed."));

        Customer loggedInCustomer = customerRepository.findByPersonEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Cart cart = loggedInCustomer.getCart();

        PickupCenter pickupCenter = pickupCenterRepository
                .findByEmail(orderRequest.getPickupCenterEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Pickup center not found"));

        BigDecimal bdTotal  = pickupCenter.getDeliveryFee()
                .add(BigDecimal.valueOf(cart.getTotal()));
        Address address = Address.builder()
                .name(orderRequest.getAddress())
                .customer(loggedInCustomer)
                .build();

        Order order = Order.builder()
                .reference(UUID.randomUUID().toString())
                .grandTotal(bdTotal)
                .deliveryFee(pickupCenter.getDeliveryFee())
                .pickupCenter(pickupCenter)
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

        cartService.clearCart("");

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Order made successfully!");
    }
}
