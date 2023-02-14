package com.richards.mealsapp.config.jwt;

import org.springframework.security.core.Authentication;

public interface TokenGeneratorService {
    String generateToken(Authentication authentication);

    String generatePasswordResetToken(String email);

    String generateVerificationToken(String email);
}
