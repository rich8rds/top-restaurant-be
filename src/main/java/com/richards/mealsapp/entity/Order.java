package com.richards.mealsapp.entity;

import com.richards.mealsapp.enums.DeliveryStatus;
import com.richards.mealsapp.enums.ModeOfDelivery;
import com.richards.mealsapp.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "order_tbl")
public class Order extends BaseEntity {
    private String reference;
    private BigDecimal grandTotal;
    private Double discount;
    private BigDecimal deliveryFee;
    @Enumerated(EnumType.STRING)
    TransactionStatus transactionStatus;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;
    @Enumerated(EnumType.STRING)
    private ModeOfDelivery modeOfDelivery;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;
    @OneToMany
    @JoinColumn(name="order_id", referencedColumnName="id")
    Set<OrderItem> orderItems = new HashSet<>();
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
