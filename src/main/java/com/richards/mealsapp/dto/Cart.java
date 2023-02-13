package com.richards.mealsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
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
