package com.richards.mealsapp.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "token_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token extends BaseEntity {
    @Column(length = 500)
    private String token;
    private Long startTime;
    private Long expirationTime;
    @OneToOne
    @JoinColumn(name = "person_id")
    private Person person;
}
