package com.richards.mealsapp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
