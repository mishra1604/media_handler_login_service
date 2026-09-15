package com.social.media.handler.controller;

import com.social.media.handler.config.ApplicationConfig;
import com.social.media.handler.model.LoginCredentialEntity;
import com.social.media.handler.model.LongLivedUserAccessTokenModel;
import com.social.media.handler.service.InstagramService;
import com.social.media.handler.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Map;

@RestController
@RequestMapping("/media/handler")
@Slf4j
@RequiredArgsConstructor
public class MediaHandlerController {

    private final LoginService loginService;
    private final InstagramService instagramService;
    private final ApplicationConfig applicationConfig;

//    public MediaHandlerController(LoginService loginService, InstagramService instagramService) {
//        this.loginService = loginService;
//        this.instagramService = instagramService;
//    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        log.info("Controller received request to check status");
        return ResponseEntity.ok("Media Handler is running");
    }

    @GetMapping("/fetch/login")
    public ResponseEntity<Object> fetchLogin(@RegisteredOAuth2AuthorizedClient("facebook") OAuth2AuthorizedClient authorizedClient) {
        log.info("Media Handler Controller received request to fetch login");

        // 1: Retrieve the short-lived access token from the authorized client
        String shortLivedAccessToken = authorizedClient.getAccessToken().getTokenValue();
        log.info("Short-lived access token: {}", shortLivedAccessToken);

        // fetch instagram profile info and save Instagram profile info with access token to database
        Map<String, Object> instagramProfileInfo = instagramService.getInstagramProfileInfo(shortLivedAccessToken);
        loginService.saveProfileAndCredentials(instagramProfileInfo, shortLivedAccessToken);
        return ResponseEntity.ok().body(instagramProfileInfo);
    }
}
