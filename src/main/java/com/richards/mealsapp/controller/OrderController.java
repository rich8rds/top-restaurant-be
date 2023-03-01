package com.richards.mealsapp.controller;

import com.richards.mealsapp.dto.OrderRequest;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class OrderController {
    private final OrderService orderService;

    //checkout --> creating an order based on the items in the cart
    @PostMapping("/order/checkout")
    public BaseResponse<String> checkoutOrders(@RequestBody OrderRequest orderRequest) {
        return orderService.checkoutOrder(orderRequest);
    }

}
