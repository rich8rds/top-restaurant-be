package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.config.jwt.TokenGeneratorService;
import com.richards.mealsapp.config.userdetails.AppUserDetailsService;
import com.richards.mealsapp.dto.LoginDto;
import com.richards.mealsapp.dto.SignupRequestDto;
import com.richards.mealsapp.entity.Customer;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Token;
import com.richards.mealsapp.enums.Gender;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.enums.UserRole;
import com.richards.mealsapp.event.RegistrationEvent;
import com.richards.mealsapp.exceptions.AlreadyExistsException;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.repository.CustomerRepository;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.repository.TokenRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final ApplicationEventPublisher publisher;
    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorService tokenService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final HttpServletRequest request;
    private final CustomerRepository customerRepository;
    private TokenRepository tokenRepository;

    @Override
    public BaseResponse<String> authenticateUser(LoginDto loginRequest) {
        try {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            if(!user.isEnabled())
                throw new UsernameNotFoundException("You have not been verified. Check your email to be verified!");

            if (!user.isAccountNonLocked()){
                BaseResponse<String> response =
                        new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS, "This account has been deactivated");
                return response;
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            if(authentication == null)
                throw new InvalidCredentialsException("Invalid Email or Password");

            BaseResponse<String> response = new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                            "Login Successful",
                            tokenService.generateToken(authentication));
            return response;

        } catch (InvalidCredentialsException e) {
            return new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS,
                    "Password or Email not correct");

        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                    "Login Successful",
                    "Password or Email not correct");
        }
    }

    @Override
    public BaseResponse<String> registerUser(SignupRequestDto signupRequestDto) {
        boolean emailExists = personRepository.existsByEmail(signupRequestDto.getEmail());
        if (emailExists)
            throw new AlreadyExistsException("User Already Exists!");

        Person person = Person.builder()
                .firstName(signupRequestDto.getFirstName())
                .lastName(signupRequestDto.getLastName())
                .email(signupRequestDto.getPassword())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(UserRole.CUSTOMER)
                .phone(signupRequestDto.getPhone())
                .address(signupRequestDto.getAddress() != null ? signupRequestDto.getAddress() : "")
                .gender(Gender.valueOf(signupRequestDto.getGender()))
                .build();

        Person savedPerson = personRepository.save(person);
        Customer customer = Customer.builder().person(savedPerson).build();
        customerRepository.save(customer);

        new Thread(() -> publisher.publishEvent(new RegistrationEvent(savedPerson, getApplicationUrl()))).start();
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS);
    }

    @Override
    public BaseResponse<String> verifyUserVerificationToken(String token) {
        //Verify token
        // Check if expired and delete
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Token Not Found"));

        Long expirationTime = verificationToken.getExpirationTime();
        long now = Instant.now().getEpochSecond();
        if(now > expirationTime)
            throw new BadCredentialsException("Token has expired. Try resending verification token to email");

        Person person = personRepository.findById(verificationToken.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        person.setIsEnabled(true);
        personRepository.save(person);

        tokenRepository.delete(verificationToken);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Verification Token Sent!");
    }

    @Override
    public BaseResponse<String> resendVerificationToken(String token) {
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Token Not Found"));

        Person person = personRepository.findById(verificationToken.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        tokenRepository.delete(verificationToken);
        new Thread(() -> publisher.publishEvent(new RegistrationEvent(person, getApplicationUrl()))).start();
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Resend Token Successful");
    }

    private String getApplicationUrl() {
        return "http://" + request.getServerName() + ":3000";
    }
}
