package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.CartRequest;
import com.richards.mealsapp.dto.CartResponse;
import com.richards.mealsapp.response.BaseResponse;

public interface CartService {
    BaseResponse<String> addItemToCart(Long productId,  String anonymousId);

    BaseResponse<String> deleteItemFromCart(Long itemId, String anonymousId);

    BaseResponse<CartResponse> getCustomerCartItems(String anonymousId);

    BaseResponse<String> addItemQuantityToCart(Long productId, String anonymousId);

    BaseResponse<String> reduceCartItemQuantity(Long productId, String anonymousId);

    BaseResponse<String> clearCart(String anonymousId);
}
