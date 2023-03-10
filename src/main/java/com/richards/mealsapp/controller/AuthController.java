package com.richards.mealsapp.controller;

import com.richards.mealsapp.dto.*;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("auth/login")
    public BaseResponse<String> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("auth/register")
    public BaseResponse<String> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        return authService.registerUser(signupRequest, request);
    }

    @GetMapping("auth/verify-registration")
    public BaseResponse<String> verifyAccount(@RequestParam String token){
        return authService.verifyUserVerificationToken(token);
    }

    @GetMapping("auth/resend-verification-token")
    public BaseResponse<String> resendVerificationToken(@RequestParam String token, HttpServletRequest request) {
        return authService.resendVerificationToken(token, request);
    }

    @PostMapping("auth/update-password")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return authService.updatePassword(updatePasswordRequest);
    }

    @PostMapping("auth/forgot-password")
    public BaseResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return authService.getForgotPasswordToken(forgotPasswordRequest, request);
    }

    @GetMapping("auth/change-password")
    public BaseResponse<String> resetPassword(@RequestParam String token,
                                              @Valid @RequestBody
                                              ChangePasswordRequest changePasswordRequest) {
        return authService.changePasswordWithToken(token, changePasswordRequest);
    }

    @PostMapping("customer/change-profile")
    public BaseResponse<ProfileResponse> updateProfile(@RequestBody ProfileRequest profileRequest) {
       return authService.updateUserProfile(profileRequest);
    }

    @GetMapping("customer/view-profile")
    public BaseResponse<ProfileResponse> getProfileDetails() {
        return authService.getUserProfile();

    }

    @PostMapping("auth/social-login")
    public BaseResponse<String> socialLogin(@Valid @RequestBody SocialLoginRequest socialLoginRequest) {
        return authService.socialLogin(socialLoginRequest);
    }
}
