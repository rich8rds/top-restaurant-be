package com.richards.mealsapp.event.listener;

import com.richards.mealsapp.config.jwt.TokenGeneratorService;
import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Token;
import com.richards.mealsapp.event.RegistrationEvent;
import com.richards.mealsapp.mail.JavaMailService;
import com.richards.mealsapp.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;

import java.io.IOException;

@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {
    private final TokenGeneratorService tokenService;
    private final JavaMailService javaMailService;

    private final TokenRepository tokenRepository;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        Person person = event.getPerson();
        String token = tokenService.generateVerificationToken(person.getEmail());

        Token dbToken = Token.builder().token(token).person(person).build();
        tokenRepository.save(dbToken);

        String text = "";
        //send mail to user
        try {
            javaMailService.sendMailAlt(person.getEmail(), "ACCOUNT VERIFICATION", text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
