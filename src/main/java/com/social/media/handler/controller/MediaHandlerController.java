package com.social.media.handler.controller;

import com.social.media.handler.service.InstagramService;
import com.social.media.handler.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/media/handler")
@Slf4j
public class MediaHandlerController {

    private final LoginService loginService;
    private final InstagramService instagramService;

    public MediaHandlerController(LoginService loginService, InstagramService instagramService) {
        this.loginService = loginService;
        this.instagramService = instagramService;
    }

    @GetMapping("/status")
    public ResponseEntity<String> getStatus() {
        log.info("Controller received request to check status");
        return ResponseEntity.ok("Media Handler is running");
    }

    @GetMapping("/facebookLogin")
    public ResponseEntity<String> loginViaFacebook(@RegisteredOAuth2AuthorizedClient("facebook") OAuth2AuthorizedClient authorizedClient) {
        log.info("Controller received request to login via Facebook");
        String response = loginService.getProfileInfo(authorizedClient);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/instagramProfile")
    public ResponseEntity<Object> getInstagramProfileInfo() {
        log.info("Controller received request to fetch Instagram profile info");
        Object response = instagramService.getInstagramProfileInfo();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/fetchInstagramBusinessAccount")
    public ResponseEntity<Object> getInstagramBusinessAccountInfo() {
        log.info("Controller received request to fetch Instagram business account info");
        String instagramBusinessAccountId = "17841439640160004"; // Replace with actual ID or fetch dynamically
        Object response = instagramService.getInstagramBusinessAccountInfo(instagramBusinessAccountId);
        return ResponseEntity.ok(response);
    }
}
