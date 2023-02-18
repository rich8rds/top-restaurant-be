package com.richards.mealsapp.dto;

import com.richards.mealsapp.enums.Gender;
import lombok.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String gender;
    private String phone;
    private String address;
}
