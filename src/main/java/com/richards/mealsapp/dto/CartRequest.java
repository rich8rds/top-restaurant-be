package com.richards.mealsapp.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CartRequest {
    private Long productId;
    private String anonymousId;
}
