package com.richards.mealsapp.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pickup_center_tbl")
public class PickupCenter extends BaseEntity {
    private String name;
    private String address;
    private String email;
    private String phone;
    private BigDecimal deliveryFee;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "state_id")
    private State state;


    @JsonIgnore
    @OneToOne(mappedBy = "pickupCenter", cascade = CascadeType.ALL)
    private Order order;
}
