package com.richards.mealsapp.event;

import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Token;
import com.richards.mealsapp.enums.EventType;
import com.richards.mealsapp.mail.JavaMailService;
import com.richards.mealsapp.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class CompleteEvent {
    private final JavaMailService javaMailService;
    private final TokenRepository tokenRepository;

    public void onApplicationEvent(MealApplicationEvent event) {
        Person person = event.getPerson();
        String token = UUID.randomUUID().toString();
        log.info("ON_APPLICATION EVENT: {}", person.getFirstName());

        Token dbToken = Token.builder().token(token)
                .startTime((System.currentTimeMillis()*1000))
                .expirationTime(Instant.now().plus(20, ChronoUnit.MINUTES).getEpochSecond())
                .person(person).build();
        tokenRepository.save(dbToken);

        String message;
        String url;

        if(event.getEventType().equals(EventType.REGISTRATION)) {
            message =
                    "<html> " +
                            "<body>" +
                            "<h5>Hi " + person.getFirstName() + " " + person.getLastName() + ",<h5> <br>" +
                            "<p>Thank you for your interest in joining Top Restaurant." +
                            "To complete your registration, we need you to verify your email address \n" +
                            "<br><a href=[[TOKEN_URL]]>CLICK TO VERIFY </a><p>" +
                            "</body> " +
                            "</html>";
            url = event.getApplicationUrl() + "verify?token=" + token;
        }

        else {
            message =
                    "<html> " +
                            "<body>" +
                            "<h5>Hi " + person.getFirstName() + " " + person.getLastName() + ",<h5> <br>" +
                            "<p>This is a request to reset your password" +
                            "This is your reset link which expires in 15 minutes.\n" +
                            "<br><a href=[[TOKEN_URL]]>CLICK TO CHANGE PASSWORD</a><p>" +
                            "</body> " +
                            "</html>";
            url = event.getApplicationUrl() + "change-password?token=" + token;
        }

        message = message.replace("[[TOKEN_URL]]", url);

        try {
            log.info("EMAIL VERIFICATION: {}", url);
            javaMailService.sendMailAlt(person.getEmail(), "ACCOUNT VERIFICATION", message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}