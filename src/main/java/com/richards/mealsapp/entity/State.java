package com.richards.mealsapp.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "state_tbl")
public class State extends BaseEntity {
    private String name;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<PickupCenter> pickupCenters;
}
