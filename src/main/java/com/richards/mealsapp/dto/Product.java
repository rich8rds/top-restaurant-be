package com.richards.mealsapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "product_tbl")
public class Product extends BaseEntity {
    private String name;
    private Integer quantity;
    private Double price;
    private Integer rating;
    private String imageUrl;
    @Column(length=1000)
    private String description;
    @OneToOne
    @JoinColumn(name = "subcategory_id")
    private Subcategory subcategory;
}
