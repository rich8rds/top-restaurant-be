package com.richards.mealsapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_tbl")
public class Cart extends BaseEntity {
    @OneToMany(cascade = CascadeType.ALL)
    private Set<CartItem> cartCartItems;
    private Double total = 0D;
    @JsonIgnore
    @OneToOne(mappedBy = "cart", cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
}
