package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.OrderRequest;
import com.richards.mealsapp.response.BaseResponse;

public interface OrderService {
    BaseResponse<String> checkoutOrder(OrderRequest orderRequest);
}
