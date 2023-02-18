package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.*;
import com.richards.mealsapp.response.BaseResponse;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {

    BaseResponse<String> authenticateUser(LoginRequest loginRequest);

    BaseResponse<String> registerUser(SignupRequest signupRequest, HttpServletRequest request);

    BaseResponse<String> verifyUserVerificationToken(String token);

    BaseResponse<String> resendVerificationToken(String email, HttpServletRequest request);

    BaseResponse<String> updatePassword(UpdatePasswordRequest updatePasswordRequest);

    BaseResponse<String> getForgotPasswordToken(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request);

    BaseResponse<String> changePasswordWithToken(String token, ChangePasswordRequest changePasswordRequest);

    BaseResponse<ProfileResponse> getUserProfile();

    BaseResponse<ProfileResponse> updateUserProfile(ProfileRequest profileRequest);
}
