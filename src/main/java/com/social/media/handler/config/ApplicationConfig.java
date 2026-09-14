package com.social.media.handler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@Getter
@Setter
public class ApplicationConfig {

    public final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v26.0";

    public String instagramBusinessAccountId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-id}")
    public String applicationId;

    @Value("${spring.security.oauth2.client.registration.facebook.client-secret}")
    public String applicationSecret;
}
