package com.richards.mealsapp;

import com.richards.mealsapp.config.jwt.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableOpenApi
@EnableConfigurationProperties(RSAKeyProperties.class)
public class MealsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealsAppApplication.class, args);
    }

}
