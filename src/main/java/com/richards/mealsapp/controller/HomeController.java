package com.richards.mealsapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    @GetMapping
    public String homepage(Principal principal) {
        if(principal != null) log.info("Username: {}", principal.getName());
        return "Welcome to the homepage!";
    }

    @GetMapping("/user")
    public Principal user(Principal principal) {
        log.info("Username: {}", principal.getName());
        return principal;
    }

    @GetMapping("/checkuser")
    public Principal checkUser(Principal principal) {
        log.info("Username: {}", principal.getName());
        return principal;
    }


}
