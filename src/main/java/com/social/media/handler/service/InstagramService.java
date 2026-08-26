package com.social.media.handler.service;

import com.social.media.handler.config.ApplicationConfig;
import com.social.media.handler.config.SecurityConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.jackson.autoconfigure.JacksonProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;

@Service
@Slf4j
public class InstagramService {

    private final SecurityConfig securityConfig;
    RestClient restClient;

    public InstagramService(SecurityConfig securityConfig, ApplicationConfig applicationConfig) {
        this.securityConfig = securityConfig;
        this.restClient = RestClient.create(applicationConfig.FACEBOOK_GRAPH_API_URL);
    }

    public String getInstagramProfileInfo() {
        String accessToken = securityConfig.getOAuthAccessToken();

        return restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/me/accounts")
                        .queryParam("access_token", accessToken)
                        .queryParam("fields", "id,name,instagram_business_account")
                        .build())
                .retrieve()
                .body(String.class);
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
