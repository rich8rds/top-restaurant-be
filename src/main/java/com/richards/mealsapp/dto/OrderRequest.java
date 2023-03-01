package com.richards.mealsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String anonymousId;
    private BigDecimal grandTotal;
//    private ModeOfPayment modeOfPayment;
    private Double deliveryFee;
    private String modeOfDelivery;
    private Double discount;
    private String address;
    private String pickupCenterEmail;
}
