package com.gonnect.ranger.policies;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;

@Configuration
public class BeanConfigurer {
    @Value("${com.gonnect.custom.authorizer}")
    private String customAuthorizer;

    @Bean
    public WebClient client() {
        return WebClient.builder()
                .baseUrl(customAuthorizer)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", customAuthorizer))
                .build();
    }
}
