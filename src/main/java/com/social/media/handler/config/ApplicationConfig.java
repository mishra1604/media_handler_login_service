package com.social.media.handler.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class ApplicationConfig {

    public final String FACEBOOK_GRAPH_API_URL = "https://graph.facebook.com/v21.0";

    public String instagramBusinessAccountId;
}
