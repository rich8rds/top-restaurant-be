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
@Table(name = "category_tbl")
public class Category extends BaseEntity {
    private String name;
    private String imageUrl;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    private Set<Subcategory> subcategories;
}
