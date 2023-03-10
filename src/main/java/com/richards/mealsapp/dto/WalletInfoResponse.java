package com.richards.mealsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WalletInfoResponse {
    private String firstName;
    private String lastName;
    private String email;
    private String walletBalance;
    private String baseCurrency;

}
