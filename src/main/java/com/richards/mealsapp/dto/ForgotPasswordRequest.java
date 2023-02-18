package com.richards.mealsapp.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ForgotPasswordRequest {
    @NotBlank(message = "Email is required")
    private String email;
}
