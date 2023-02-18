package com.richards.mealsapp.entity;

import com.richards.mealsapp.enums.Gender;
import com.richards.mealsapp.enums.UserRole;
import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person_tbl")
public class Person extends BaseEntity {
    @Column(nullable = false, length = 101)
    private String firstName;
    @Column(nullable = false, length = 101)
    private String lastName;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    private UserRole role;

    private String phone;

    private Boolean isEnabled;

    private Boolean isAccountLocked;

    private String address;

//    @JsonIgnore
//    @OneToOne(mappedBy = "person", cascade = CascadeType.ALL)
//    @JoinColumn(name = "customer_id")
//    private Customer customer;
}
