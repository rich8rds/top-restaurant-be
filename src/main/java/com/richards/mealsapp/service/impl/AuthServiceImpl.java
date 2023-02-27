package com.richards.mealsapp.service.impl;

import com.richards.mealsapp.config.jwt.TokenGeneratorService;
import com.richards.mealsapp.config.userdetails.AppUserDetailsService;
import com.richards.mealsapp.dto.*;
import com.richards.mealsapp.entity.Customer;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Token;
import com.richards.mealsapp.enums.EventType;
import com.richards.mealsapp.enums.Gender;
import com.richards.mealsapp.enums.ResponseCodeEnum;
import com.richards.mealsapp.enums.UserRole;
import com.richards.mealsapp.event.CompleteEvent;
import com.richards.mealsapp.event.MealApplicationEvent;
import com.richards.mealsapp.exceptions.AlreadyExistsException;
import com.richards.mealsapp.exceptions.PasswordMisMatchException;
import com.richards.mealsapp.exceptions.ResourceNotFoundException;
import com.richards.mealsapp.mail.JavaMailService;
import com.richards.mealsapp.repository.CustomerRepository;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.repository.TokenRepository;
import com.richards.mealsapp.response.BaseResponse;
import com.richards.mealsapp.service.AuthService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.auth.InvalidCredentialsException;
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

import static com.richards.mealsapp.enums.EventType.*;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JavaMailService javaMailService;
    private final AppUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenGeneratorService tokenService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final TokenRepository tokenRepository;

    @Override
    public BaseResponse<String> authenticateUser(LoginRequest loginRequest) {
            UserDetails user = userDetailsService.loadUserByUsername(loginRequest.getEmail());

            if(!user.isEnabled()) {
                return new BaseResponse<>(ResponseCodeEnum.ERROR, "You have not been verified. Check your email to be verified!");
            }

            if (!user.isAccountNonLocked())
                return new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS, "This account has been deactivated");
            return authenticateUserWithAuthManager(loginRequest);
    }

    private BaseResponse<String> authenticateUserWithAuthManager(LoginRequest loginRequest) {
        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            if(authentication == null)
                throw new InvalidCredentialsException("Invalid Email or Password");

            return new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                    "Login Successful",
                    tokenService.generateToken(authentication));

        } catch (InvalidCredentialsException e) {
            return new BaseResponse<>(ResponseCodeEnum.UNAUTHORISED_ACCESS, e.getMessage());
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return new BaseResponse<>(ResponseCodeEnum.ERROR.getCode(), "Login Failed",
                    "Password or Email not correct");
        }
    }

    @Override
    public BaseResponse<String> registerUser(SignupRequest signupRequest, HttpServletRequest request) {
        boolean emailExists = personRepository.existsByEmail(signupRequest.getEmail());
        if (emailExists) {
            System.out.println("EMAIL EXISTS");
            throw new AlreadyExistsException("User Already Exists!");
        }

        Person person = Person.builder()
                .firstName(signupRequest.getFirstName())
                .lastName(signupRequest.getLastName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .role(UserRole.CUSTOMER)
                .phone(signupRequest.getPhone())
                .isAccountLocked(true)
                .isEnabled(false)
                .address(signupRequest.getAddress() != null ? signupRequest.getAddress() : "")
                .gender(Gender.valueOf(signupRequest.getGender() == null ? Gender.OTHER.name() : signupRequest.getGender()))
                .build();

        Person savedPerson = personRepository.save(person);
        Customer customer = Customer.builder().person(savedPerson).build();
        customerRepository.save(customer);

        completeEvent(savedPerson, REGISTRATION, request);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Check your email to get verified");
    }

    @Override
    public BaseResponse<String> verifyUserVerificationToken(String token) {
        Token verificationToken = getToken(token);
        Person person = personRepository.findById(verificationToken.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        person.setIsEnabled(true);
        personRepository.save(person);

//        tokenRepository.delete(verificationToken);

        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Email Verified!");
    }

    @Override
    public BaseResponse<String> resendVerificationToken(String token, HttpServletRequest request) {
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Token Not Found"));

        Person person = personRepository.findById(verificationToken.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        tokenRepository.delete(verificationToken);
        completeEvent(person, REGISTRATION, request);
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Resend Token Successful");
    }

    @Override
    public BaseResponse<String> updatePassword(UpdatePasswordRequest updatePassword) {
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        String oldPassword = updatePassword.getOldPassword();
        String newPassword = updatePassword.getNewPassword();
        String confirmNewPassword = updatePassword.getConfirmNewPassword();

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        String dbPassword = person.getPassword();
        if (!passwordEncoder.matches(oldPassword, dbPassword))
            throw new PasswordMisMatchException("Passwords do not match!");
        if(!confirmNewPassword.equals(newPassword))
            updatePassword.setConfirmNewPassword(newPassword);

        person.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(person);
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(), "Password Successfully Changed");

    }

    @Override
    public BaseResponse<String> getForgotPasswordToken(ForgotPasswordRequest forgotPasswordRequest, HttpServletRequest request) {
        Person person = personRepository.findByEmail(forgotPasswordRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        completeEvent(person, FORGOTPASSWORD, request);
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(), "Check your email to change password.");
    }

    @Override
    public BaseResponse<String> changePasswordWithToken(String token, ChangePasswordRequest changePasswordRequest) {
        Token verificationToken = getToken(token);

        Person person = personRepository.findById(verificationToken.getPerson().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User with email does not exist"));

        String newPassword = changePasswordRequest.getNewPassword();
        person.setPassword(passwordEncoder.encode(newPassword));
        personRepository.save(person);
        tokenRepository.delete(verificationToken);


        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, "Verification Token Sent!");
    }

    @Override
    public BaseResponse<ProfileResponse> getUserProfile() {
        String email = UserUtil.extractEmailFromPrincipal()
            .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User With Email Does Not Exist"));

        ProfileResponse profileResponse = ProfileResponse.builder()
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .phone(person.getPhone())
                .gender(person.getGender().toString())
                .address(person.getAddress())
                .build();
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS, profileResponse);
    }

    @Override
    public BaseResponse<ProfileResponse> updateUserProfile(ProfileRequest profileRequest) {
        String email = UserUtil.extractEmailFromPrincipal()
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        Person person = personRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Does Not Exist"));

        String firstName = profileRequest.getFirstName();
        String lastName = profileRequest.getLastName();
        String requestEmail = profileRequest.getEmail();
        String phone = profileRequest.getPhone();
        String password = profileRequest.getPassword();
        String address = profileRequest.getAddress();

        if(firstName != null) person.setFirstName(firstName);
        if(lastName != null) person.setLastName(lastName);
        if(requestEmail != null) {
            if(!personRepository.existsByEmail(requestEmail))
                person.setEmail(requestEmail);
        }


        if(profileRequest.getGender() != null) {
            Gender gender = Gender.valueOf(profileRequest.getGender());
            person.setGender(gender);
        }

        if(phone != null) person.setPhone(phone);
        if(password != null) person.setPassword(password);
        if(address != null) person.setAddress(address);

        Person savedPerson = personRepository.save(person);
        ProfileResponse profileResponse = ProfileResponse.builder()
                .firstName(savedPerson.getFirstName())
                .lastName(savedPerson.getLastName())
                .email(savedPerson.getEmail())
                .phone(savedPerson.getPhone())
                .gender(savedPerson.getGender().toString())
                .address(savedPerson.getAddress())
                .build();
        return new BaseResponse<>(ResponseCodeEnum.SUCCESS.getCode(),
                "Profile successfully updated",
                profileResponse);
    }

    @Override
    public BaseResponse<String> socialLogin(SocialLoginRequest socialLoginRequest) {
        if(!personRepository.existsByEmail(socialLoginRequest.getEmail())) {
            String firstName = socialLoginRequest.getFirstName();
            String lastName = socialLoginRequest.getLastName();
            String email = socialLoginRequest.getEmail();

            Person createUser = new Person();
            createUser.setFirstName(firstName);
            createUser.setLastName(lastName);
            createUser.setEmail(email);
            createUser.setPassword(passwordEncoder.encode("myNewLoginDetails"));
            createUser.setIsEnabled(true);
            personRepository.save(createUser);
        }
        LoginRequest loginRequest = new LoginRequest(socialLoginRequest.getEmail(), "myNewLoginDetails");
        return authenticateUserWithAuthManager(loginRequest);
    }


    private Token getToken(String token) {
        Token verificationToken = tokenRepository.findByToken(token).orElseThrow(
                () -> new ResourceNotFoundException("Token Not Found"));

        Long expirationTime = verificationToken.getExpirationTime();
        long now = Instant.now().getEpochSecond();
        if(now > expirationTime)
            throw new BadCredentialsException("Token has expired. Try resending verification token to email");

        return verificationToken;
    }
    private String getApplicationUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":3000";
        return "http://" + request.getServerName() + ":" + request.getServerPort() + "/api/v1/auth/";
    }

    private void completeEvent(Person savedPerson, EventType eventType, HttpServletRequest request) {
        new Thread(() -> {
            MealApplicationEvent mealApplicationEvent = new MealApplicationEvent(savedPerson, getApplicationUrl(request), eventType);
            CompleteEvent completeEvent = new CompleteEvent(javaMailService, tokenRepository);
            completeEvent.onApplicationEvent(mealApplicationEvent);
        }).start();
    }
}
