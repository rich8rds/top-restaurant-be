package com.richards.mealsapp.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cart_item_tbl")
public class CartItem extends BaseEntity {
    private String productName;
    private String imageUrl;
    private Integer orderedQty;
    private BigDecimal unitPrice;
    private BigDecimal subTotal;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
    @OneToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


}
