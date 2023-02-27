package com.richards.mealsapp.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocialLoginRequest {
    @NotBlank(message = "First name cannot be blank")
    private  String firstName;
    @NotBlank(message = "Last name cannot be blank")
    private  String lastName;
    @Email(message = "Email name cannot be blank")
    private  String email;
    @NotBlank(message = "Confirm password name cannot be blank")
    private  String password;
    private  String profileUrl;

}
