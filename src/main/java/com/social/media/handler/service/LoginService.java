package com.social.media.handler.service;

import com.social.media.handler.config.ApplicationConfig;
import com.social.media.handler.config.SecurityConfig;
import com.social.media.handler.exception.AccessTokenRetrievalFailureException;
import com.social.media.handler.model.LongLivedUserAccessTokenModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import com.fasterxml.jackson.databind.JsonNode;

@Service
@Slf4j
public class LoginService {

    private final SecurityConfig securityConfig;
    private final RestClient faceBookGraphApiRestClient;
    private final ApplicationConfig appConfig;

    public LoginService(SecurityConfig securityConfig,
                         @Qualifier("facebookGraphApiRestClient") RestClient faceBookGraphApiRestClient,
                         ApplicationConfig appConfig) {
        this.securityConfig = securityConfig;
        this.appConfig = appConfig;
        this.faceBookGraphApiRestClient = faceBookGraphApiRestClient;
    }

    public LongLivedUserAccessTokenModel retrieveLongAccessToken(String shortLivedAccessToken) {
        log.info("Exchanging short-lived access token for long-lived access token");

        LongLivedUserAccessTokenModel response = faceBookGraphApiRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth/access_token")
                        .queryParam("grant_type", "fb_exchange_token")
                        .queryParam("client_id", appConfig.applicationId)
                        .queryParam("client_secret", appConfig.applicationSecret)
                        .queryParam("fb_exchange_token", shortLivedAccessToken)
                        .build())
                .retrieve()
                .body(LongLivedUserAccessTokenModel.class);

        String longLivedAccessToken = response != null ? response.getAccessToken() : null;
        if (longLivedAccessToken == null) {
            log.error("Failed to retrieve long-lived access token");
            throw new AccessTokenRetrievalFailureException("Failed to retrieve long-lived access token");
        }
        log.info("long lived access token: {}", longLivedAccessToken);
        return response;
    }

}
