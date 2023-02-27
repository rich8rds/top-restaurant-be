package com.richards.mealsapp.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "payment_tbl")
public class Payment extends BaseEntity {
    private String reference;
    private Boolean success;
    private String email;
}
