package com.richards.mealsapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "address_tbl")
public class Address extends BaseEntity {
    private String fullName;
    private String phone;
    private String emailAddress;
    private String street;
    private String city;
    private String state;
    private String country;
    private Boolean isDefault;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
