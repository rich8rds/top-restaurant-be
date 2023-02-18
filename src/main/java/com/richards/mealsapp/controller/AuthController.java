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
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public BaseResponse<String> authenticate(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("signup")
    public BaseResponse<String> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletRequest request) {
        return authService.registerUser(signupRequest, request);
    }

    @GetMapping("verifyRegistration")
    public BaseResponse<String> verifyAccount(@RequestParam("token") String token){
        return authService.verifyUserVerificationToken(token);
    }

    @GetMapping("resendVerificationToken")
    public BaseResponse<String> resendVerificationToken(@RequestParam("token") String token, HttpServletRequest request) {
        return authService.resendVerificationToken(token, request);
    }

    @PostMapping("updatePassword")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) {
        return authService.updatePassword(updatePasswordRequest);
    }

    @PostMapping("forgotPassword")
    public BaseResponse<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        return authService.getForgotPasswordToken(forgotPasswordRequest, request);
    }

    @GetMapping("changePassword")
    public BaseResponse<String> resetPassword(@RequestParam("token") String token,
                                              @Valid @RequestBody
                                              ChangePasswordRequest changePasswordRequest) {
        return authService.changePasswordWithToken(token, changePasswordRequest);
    }

    @PostMapping("changeProfile")
    public BaseResponse<ProfileResponse> updateProfile(@RequestBody ProfileRequest profileRequest) {
       return authService.updateUserProfile(profileRequest);
    }

    @GetMapping("viewProfile")
    public BaseResponse<ProfileResponse> getProfileDetails() {
        return authService.getUserProfile();

    }
}
