package com.richards.mealsapp.entity;

import com.richards.mealsapp.enums.DeliveryStatus;
import com.richards.mealsapp.enums.ModeOfDelivery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;


import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "order_tbl")
public class Order extends BaseEntity {
    private Double grandTotal;
    private Double discount;
    private Double deliveryFee;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Enumerated(EnumType.STRING)
    private ModeOfDelivery modeOfDelivery;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pickup_id")
    private PickupCenter pickupCenter;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
