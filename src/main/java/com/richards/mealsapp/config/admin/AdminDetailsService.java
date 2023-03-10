package com.richards.mealsapp.config.admin;

import com.richards.mealsapp.entity.Person;
import com.richards.mealsapp.entity.SuperAdmin;
import com.richards.mealsapp.entity.Wallet;
import com.richards.mealsapp.enums.BaseCurrency;
import com.richards.mealsapp.enums.Gender;
import com.richards.mealsapp.exceptions.UnauthorizedException;
import com.richards.mealsapp.repository.PersonRepository;
import com.richards.mealsapp.repository.ProductRepository;
import com.richards.mealsapp.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

import static com.richards.mealsapp.enums.UserRole.SUPERADMIN;

@RequiredArgsConstructor
@Slf4j
@Component
public class AdminDetailsService implements CommandLineRunner {
    private final WalletRepository walletRepository;
    private final SuperAdminRepository superAdminRepository;
    private final PasswordEncoder passwordEncoder;

    private final PersonRepository personRepository;


    @Value("${spring.admin.super.email}")
    private String adminEmail;

    @Value("${spring.admin.super.currency}")
    private String currency;
    String password = "password123453";


    public void initAdmin() {

        if (superAdminRepository.findAll().size() > 0) {
            throw new UnauthorizedException("Operation NOT ALLOWED");
        }

        Person person = Person.builder()
//                .date_of_birth(new Date().toString())
                .email(adminEmail)
                .firstName("super")
                .lastName("Admin")
                .role(SUPERADMIN)
                .gender(Gender.OTHER)
                .isEnabled(true)
                .password(passwordEncoder.encode(password))
                .isAccountLocked(true)
                .build();

        Wallet wallet = Wallet.builder()
                .accountBalance(BigDecimal.ZERO)
                .baseCurrency(BaseCurrency.valueOf(currency))
                .build();
        SuperAdmin superAdmin = SuperAdmin.builder()
                .person(person)
                .wallet(wallet)
                .build();

        SuperAdmin superAdminDB = superAdminRepository.save(superAdmin);
        personRepository.save(person);
        log.info("#############################################################################");
        log.info("Super Admin Details");

        log.info("Username/Email: \t" + password);
        log.info("Password/Passphrase: \t" + adminEmail);

        log.info("*******************   Wallet Information   *****************");
        log.info(superAdminDB.getWallet().toString());

        log.info("#############################################################################");

    }

    @Override
    public void run(String... args) throws Exception {
        initAdmin();
    }
}

