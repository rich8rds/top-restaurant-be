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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@RequiredArgsConstructor
public class RegistrationEventListener implements ApplicationListener<RegistrationEvent> {
    private final TokenGeneratorService tokenService;
    private final JavaMailService javaMailService;

    private final TokenRepository tokenRepository;
    @Override
    public void onApplicationEvent(RegistrationEvent event) {
        Person person = event.getPerson();
        String token = UUID.randomUUID().toString();

        Token dbToken = Token.builder().token(token)
                .startTime((System.currentTimeMillis()*1000))
                .expirationTime(Instant.now().plus(20, ChronoUnit.MINUTES).getEpochSecond())
                .person(person).build();
        tokenRepository.save(dbToken);

        String message =
                "<html> " +
                    "<body>" +
                    "<h5>Hi " + person.getFirstName() + " " + person.getLastName() +",<h5> <br>" +
                    "<p>Thank you for your interest in joining Top Restaurant." +
                    "To complete your registration, we need you to verify your email address \n" +
                    "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY AGAIN</a><p>" +
                    "</body> " +
                "</html>";

        String url = event.getApplicatiionUrl() + "/verifyRegistration?token=" + token;

        message = message.replace("[[TOKEN_URL]]", url);

        try {
            javaMailService.sendMailAlt(person.getEmail(), "ACCOUNT VERIFICATION", message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
