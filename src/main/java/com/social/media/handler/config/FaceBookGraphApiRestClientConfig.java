package com.social.media.handler.config;

import com.social.media.handler.exception.AccessTokenRetrievalFailureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
@Slf4j
public class FaceBookGraphApiRestClientConfig {
//    @Bean
//    public RestClient restClient() {
//        return RestClient.builder().build();
//    }

    @Bean
    public RestClient facebookGraphApiRestClient(RestClient.Builder builder, ApplicationConfig applicationConfig) {
        JacksonJsonHttpMessageConverter jacksonConverter = new JacksonJsonHttpMessageConverter();
        jacksonConverter.setSupportedMediaTypes(List.of(
                MediaType.APPLICATION_JSON,
                MediaType.valueOf("text/javascript") // Meta's quirky content-type
        ));

        return builder
                .baseUrl(applicationConfig.getFACEBOOK_GRAPH_API_URL())
                .configureMessageConverters(converters -> converters
                        .registerDefaults()
                        .withJsonConverter(jacksonConverter))
                .defaultStatusHandler(HttpStatusCode::isError, this::handleGraphApiError)
                .build();
    }

    private void handleGraphApiError(HttpRequest request, ClientHttpResponse response) throws IOException {
        String body = new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8);
        log.error("Graph API error [{}]: {}", response.getStatusCode(), body);
        throw new AccessTokenRetrievalFailureException(
                "Graph API call failed with status " + response.getStatusCode());
    }
}
