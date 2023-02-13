package com.richards.mealsapp.entity;

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
@Table(name = "pickup_center_tbl")
public class PickupCenter extends BaseEntity {
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "state_id")
    private State state;

    @JsonIgnore
    @OneToOne(mappedBy = "pickupCenter", cascade = CascadeType.ALL)
    private Order order;
}
