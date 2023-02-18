package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.LoginDto;
import com.richards.mealsapp.dto.SignupRequestDto;
import com.richards.mealsapp.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    BaseResponse<String> authenticateUser(LoginDto loginRequest);

    BaseResponse<String> registerUser(SignupRequestDto signupRequestDto);

    BaseResponse<String> verifyUserVerificationToken(String token);

    BaseResponse<String> resendVerificationToken(String email);
}
