package com.richards.mealsapp.controller;

import com.richards.mealsapp.config.jwt.TokenGeneratorService;
import com.richards.mealsapp.config.userdetails.AppUserDetailsService;
import com.richards.mealsapp.dto.LoginDto;
import com.richards.mealsapp.dto.SignupRequestDto;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.event.RegistrationEvent;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.AuthService;
import com.richards.mealsapp.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/")
public class AuthController {
    private final AuthService authService;

    @PostMapping("login")
    public ResponseEntity<BaseResponse<String>> authenticate(@Valid @RequestBody LoginDto loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("signup")
    public ResponseEntity<BaseResponse<String>> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        ResponseEntity<BaseResponse<String>> response = authService.registerUser(signupRequestDto);
        return response;
//        return new ResponseEntity<>("Registration Successful! Check your mail for activation link"),HttpStatus.CREATED);
    }

    @GetMapping("verifyRegistration")
    public ResponseEntity<BaseResponse<String>> verifyAccount(@RequestParam("token") String token){
        return authService.verifyUserVerificationToken(token);
    }

    @GetMapping("resendVerificationToken")
    public ResponseEntity<BaseResponse<String>> resendVerificationToken(@RequestParam("email") String email) {
        return authService.resendVerificationToken(email);
    }


}
