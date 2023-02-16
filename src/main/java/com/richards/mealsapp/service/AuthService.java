package com.richards.mealsapp.service;

import com.richards.mealsapp.dto.LoginDto;
import com.richards.mealsapp.dto.SignupRequestDto;
import com.richards.mealsapp.response.BaseResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<BaseResponse<String>> authenticateUser(LoginDto loginRequest);

    ResponseEntity<BaseResponse<String>> registerUser(SignupRequestDto signupRequestDto);

    ResponseEntity<BaseResponse<String>> verifyUserVerificationToken(String token);

    ResponseEntity<BaseResponse<String>> resendVerificationToken(String email);
}
