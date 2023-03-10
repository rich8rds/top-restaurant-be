package com.richards.mealsapp.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.richards.mealsapp.enums.BaseCurrency;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "wallet_tbl")
public class Wallet extends BaseEntity {
    private BigDecimal accountBalance;
    @Enumerated(EnumType.STRING)
    private BaseCurrency baseCurrency;

    @JsonIgnore
    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Transaction> transactions;

    @JsonIgnore
    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private Customer customer;

    @JsonIgnore
    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
    private SuperAdmin superAdmin;

}
