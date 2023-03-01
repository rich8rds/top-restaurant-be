package com.richards.mealsapp.entity;


import com.richards.mealsapp.enums.PaymentPurpose;
import com.richards.mealsapp.enums.TransactionStatus;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "transaction_tbl")
public class Transaction extends BaseEntity{

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "transaction_id")
    private Wallet wallet;

    private String amount;

    private String reference;

    @Enumerated(EnumType.STRING)
    private PaymentPurpose purpose;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

}
