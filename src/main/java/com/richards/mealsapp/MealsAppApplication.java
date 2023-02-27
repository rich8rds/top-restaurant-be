package com.richards.mealsapp;

import com.richards.mealsapp.config.jwt.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableOpenApi
@EnableConfigurationProperties(RSAKeyProperties.class)
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
public class MealsAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(MealsAppApplication.class, args);
    }

}
