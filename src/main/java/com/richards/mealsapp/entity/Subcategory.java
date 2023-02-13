package com.richards.mealsapp.entity;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subcategory_tbl")
public class Subcategory extends BaseEntity {
    private String name;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Category category;
}
