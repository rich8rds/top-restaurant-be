package com.richards.mealsapp.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "category_tbl")
public class Category extends BaseEntity {
    private String name;
    private String imageUrl;
    @JsonIgnore
    @OneToMany
    private Set<Subcategory> subcategories;
}
