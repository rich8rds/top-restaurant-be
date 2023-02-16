package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.config.jwt.TokenGeneratorService;
import com.richards.mealsapp.config.userdetails.AppUserDetailsService;
import com.richards.mealsapp.dto.LoginDto;
import com.richards.mealsapp.dto.SignupRequestDto;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.event.RegistrationEvent;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ApplicationEventPublisher publisher;
    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorService tokenService;
    private final PersonRepository personRepository;

    @Override
    public ResponseEntity<BaseResponse<String>> authenticateUser(LoginDto loginRequest) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            if(!user.isEnabled())
                throw new UsernameNotFoundException("You have not been verified. Check your email to be verified!");

            if (!user.isAccountNonLocked()){
                BaseResponse<String> response =
                        new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS, "This account has been deactivated");
                return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            if(authentication == null)
                throw new InvalidCredentialsException("Invalid Email or Password");

            BaseResponse<String> response = new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                            "Login Successful",
                            tokenService.generateToken(authentication));
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS,
                    "Password or Email not correct"),
                    HttpStatus.UNAUTHORIZED);

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return new ResponseEntity<>(new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                    "Login Successful",
                    "Password or Email not correct"),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Override
    public ResponseEntity<BaseResponse<String>> registerUser(SignupRequestDto signupRequestDto) {
        Person person = personRepository.save(new Person());
        publisher.publishEvent(new RegistrationEvent(person, "url"));
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse<String>> verifyUserVerificationToken(String token) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResponse<String>> resendVerificationToken(String email) {
        return null;
    }
}
