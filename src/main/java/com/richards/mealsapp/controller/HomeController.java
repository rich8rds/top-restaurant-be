package com.richards.mealsapp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    @GetMapping
    public Map<String, Object> homepage(OAuth2AuthenticationToken oAuth2AuthenticationToken) {
        return oAuth2AuthenticationToken.getPrincipal().getAttributes();
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
