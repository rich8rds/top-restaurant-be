package com.richards.mealsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "customer_tbl")
public class Customer extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(referencedColumnName = "customer", name = "person_id")
    private Person person;

    @OneToOne(cascade = CascadeType.ALL)
    private Cart cart;
    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Product> favorites = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Address> addresses;
}
