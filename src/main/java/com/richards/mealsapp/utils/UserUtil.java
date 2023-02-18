package com.richards.mealsapp.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Optional;

@Slf4j
public class UserUtil {
    @Bean
    public static Optional<String> extractEmailFromPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("AUTHENTICATION: {}", authentication);
        if (!(authentication instanceof AnonymousAuthenticationToken))
            return Optional.of(authentication.getName());
        return Optional.empty();
    }

    public static String formatToLocale(BigDecimal amount) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("en", "NG"));
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("¤###,###.00");

        String currencyString = df.format(amount);

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("en", "NG"));
        symbols.setCurrencySymbol("₦");
        return currencyString;
    }
}
