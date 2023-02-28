package com.richards.mealsapp.dto;

import com.richards.mealsapp.entity.CartItem;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartResponse {
   private List<CartItem> cartItems;
   private String totalAmount;

}
