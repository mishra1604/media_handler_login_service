package com.social.media.handler.service;

import com.social.media.handler.config.ApplicationConfig;
import com.social.media.handler.config.SecurityConfig;
import com.social.media.handler.exception.AccessTokenRetrievalFailureException;
import com.social.media.handler.model.LoginCredentialEntity;
import com.social.media.handler.model.LongLivedUserAccessTokenModel;
import com.social.media.handler.repository.LoginCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class LoginService {

    private final SecurityConfig securityConfig;
    private final RestClient faceBookGraphApiRestClient;
    private final ApplicationConfig appConfig;
    private final LoginCredentialRepository loginCredentialRepository;

    public LoginService(SecurityConfig securityConfig,
                         @Qualifier("facebookGraphApiRestClient")  RestClient faceBookGraphApiRestClient,
                         ApplicationConfig appConfig,
                         LoginCredentialRepository loginCredentialRepository) {
        this.securityConfig = securityConfig;
        this.appConfig = appConfig;
        this.faceBookGraphApiRestClient = faceBookGraphApiRestClient;
        this.loginCredentialRepository = loginCredentialRepository;
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

    public LoginCredentialEntity saveProfileAndCredentials(Map instagramProfileInfo, String accessToken) {
        ArrayList<HashMap<String, Object>> profileData = (ArrayList<HashMap<String, Object>>) instagramProfileInfo.get("data");
        Map<String, Object> instaProfileMap = profileData.get(0);
        Map<String, Object> instaBusinessAccountDetailMap = (Map<String, Object>) instaProfileMap.get("instagram_business_account");
        String instagramBusinessAccountId = (String) instaBusinessAccountDetailMap.get("id");
        String facebookAccountId          = (String) instaProfileMap.get("id");

        LoginCredentialEntity credentialEntity = new LoginCredentialEntity().builder()
                .instagramId(instagramBusinessAccountId)
                .facebookId(facebookAccountId)
                .accessToken(accessToken)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        log.info("Credential Entity: {}", credentialEntity.toString());
        loginCredentialRepository.save(credentialEntity);
        return credentialEntity;
    }
}
