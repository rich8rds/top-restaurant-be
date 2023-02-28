package com.richards.mealsapp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartItemRequest {
    private Long itemId;
    private String anonymousId;
}
