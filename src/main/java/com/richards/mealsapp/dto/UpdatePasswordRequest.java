package com.richards.mealsapp.dto;

import com.richards.mealsapp.annotations.PasswordValueMatch;
import com.richards.mealsapp.annotations.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
public class UpdatePasswordRequest {
    @NotBlank(message="This field is required")
    private String oldPassword;
    @ValidPassword
    private String newPassword;
    @ValidPassword
    private String confirmNewPassword;

}
