package com.richards.mealsapp.dto;

import com.richards.mealsapp.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {
    private String firstName;
    private String lastName;
    private String email;
    private Gender gender;
    private String date_of_birth;
    private String phoneNumber;
    private Boolean verificationStatus;
    private String address;
}
