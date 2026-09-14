package com.social.media.handler.service;

import com.social.media.handler.config.ApplicationConfig;
import com.social.media.handler.config.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@Slf4j
public class InstagramService {

    private final SecurityConfig securityConfig;
    private final RestClient restClient;

    public InstagramService(SecurityConfig securityConfig, ApplicationConfig applicationConfig, @Qualifier("facebookGraphApiRestClient") RestClient restClient) {
        this.securityConfig = securityConfig;
        this.restClient = restClient;
    }

    public @Nullable Map getInstagramProfileInfo(String accessToken) {
        log.info("Fetching Instagram profile info for user");
        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me/accounts")
                        .queryParam("access_token", accessToken)
                        .queryParam("fields", "id,name,instagram_business_account")
                        .build())
                .retrieve()
                .body(Map.class);
    }

    public Object getInstagramBusinessAccountInfo(String instagramBusinessAccountId) {
        String accessToken = securityConfig.getOAuthAccessToken();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/{account_id}")
                        .queryParam("access_token", accessToken)
                        .queryParam("fields", "followers_count,media_count,username")
                        .build(instagramBusinessAccountId))
                .retrieve()
                .body(String.class);
    }
}
