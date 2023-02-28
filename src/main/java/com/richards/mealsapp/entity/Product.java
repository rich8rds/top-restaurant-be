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
    private Integer availableQty;
    private Double price;
    private Integer rating;
    private String imageUrl;
    @Column(length=1000)
    private String description;
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
