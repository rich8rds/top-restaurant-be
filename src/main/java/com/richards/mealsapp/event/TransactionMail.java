package com.richards.mealsapp.event;

import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.Wallet;
import com.richards.mealsapp.mail.JavaMailService;
import com.richards.mealsapp.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.math.BigDecimal;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class TransactionMail {
    private final JavaMailService javaMailService;

    public String sendTransactionMail(Person person, BigDecimal amount, BigDecimal accountBalance) {
        String message =
                "<html> " +
                        "<body>" +
                        "<h5>Hi " + person.getFirstName() + " " + person.getLastName() + ",<h5> <br>" +
                        "<p> Your wallet has been credited with " + amount + ". Your new balance is now " +
                            accountBalance + "<p>" +
                        "</body> " +
                        "</html>";


        try {
            javaMailService.sendMailAlt(person.getEmail(), "WALLET DEPOSIT", message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Mail Sent";
    }

    public String sendWalletBalance(Person person, Wallet wallet) {
        String message =
                "<html> " +
                        "<body>" +
                        "<h5>Hi " + person.getFirstName() + " " + person.getLastName() + ",<h5> <br>" +
                        "<p> " +
                        "Your wallet balance is " + wallet.getBaseCurrency() +
                         UserUtil.formatToLocale(wallet.getAccountBalance())+
                        "<p>" +
                        "</body> " +
                        "</html>";


        try {
            javaMailService.sendMailAlt(person.getEmail(), "WALLET BALANCE", message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "Mail Sent";
    }
}
