package com.richards.mealsapp.config.jwt;

import org.springframework.security.core.Authentication;

public class TokenGeneratorServiceImpl implements TokenGeneratorService {
    @Override
    public String generateToken(Authentication authentication) {
        return null;
    }

    @Override
    public String generatePasswordResetToken(String email) {
        return null;
    }

    @Override
    public String generateVerificationToken(String email) {
        return null;
    }
}
