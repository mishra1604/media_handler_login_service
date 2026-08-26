package com.social.media.handler.service;

import com.social.media.handler.config.SecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final SecurityConfig securityConfig;
    private final RestClient restClient = RestClient.create("https://graph.facebook.com/v21.0");


    public String getProfileInfo(@RegisteredOAuth2AuthorizedClient("facebook")OAuth2AuthorizedClient authorizedClient) {
        log.info("Fetching profile info for user: {}", authorizedClient.getPrincipalName());
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        securityConfig.setOAuthAccessToken(accessToken);

        String pagesResponse = restClient.get()
                .uri("/me/accounts?access_token={token}", accessToken)
                .retrieve()
                .body(String.class);
        return "Successfully logged in! Here is your raw Pages data to parse: " + pagesResponse;
    }
}
