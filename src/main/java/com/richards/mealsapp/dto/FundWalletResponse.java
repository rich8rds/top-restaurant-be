package com.richards.mealsapp.dto;

import lombok.*;

import java.math.BigDecimal;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FundWalletResponse {
    private String fullName;
    private BigDecimal depositAmount;
    private BigDecimal newBalance;

}
