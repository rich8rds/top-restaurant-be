package com.richards.mealsapp.controller;

import com.richards.mealsapp.dto.CartItemRequest;
import com.richards.mealsapp.dto.CartRequest;
import com.richards.mealsapp.dto.CartResponse;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class CartController {
    private final CartService cartService;

    //add item to cart
    @PostMapping("/cart/add/{productId}")
    public BaseResponse<String> addItemToCart(@PathVariable Long productId,
                                              @RequestParam(defaultValue = "") String anonymousId) {
        return cartService.addItemToCart(productId, anonymousId);
    }
    //delete item from cart
    @DeleteMapping("/cart/delete/{itemId}")
    public BaseResponse<String> deleteItemFromCart(@PathVariable Long itemId,
                                                   @RequestParam(defaultValue = "") String anonymousId) {
        return cartService.deleteItemFromCart(itemId, anonymousId);
    }
    //fetch items from cart
    @GetMapping("/cart/get-items")
    public BaseResponse<CartResponse> getCartItems(@RequestParam(defaultValue = "") String anonymousId) {
        return cartService.getCustomerCartItems(anonymousId);
    }
    //increase item in cart
    @PostMapping("/cart/add-item/{productId}")
    public BaseResponse<String> addCartItemQuantity(@PathVariable Long productId,
                                                    @RequestParam(defaultValue = "") String anonymousId) {
        return cartService.addItemQuantityToCart(productId, anonymousId);
    }

    //decrease item in cart
    @PostMapping("/cart/remove-item/{productId}")
    public BaseResponse<String> deleteCartItemQuantity(@PathVariable Long productId,
                                                       @RequestParam(defaultValue = "") String anonymousId) {
        return cartService.reduceCartItemQuantity(productId, anonymousId);
    }

}
