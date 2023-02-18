package com.richards.mealsapp.dto;

import com.richards.mealsapp.annotations.PasswordValueMatch;
import com.richards.mealsapp.annotations.ValidPassword;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
//@Builder
@RequiredArgsConstructor
@PasswordValueMatch.List({
        @PasswordValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
        )
})
public class ChangePasswordRequest {
    @NotBlank(message="This field is required")
    @ValidPassword
    private String newPassword;
    @NotBlank(message="This field is required")
    @ValidPassword
    private String confirmNewPassword;

}
