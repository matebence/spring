package com.bence.mate.spring.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Rest {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().basicAuthentication("admin", "admin").build();
    }
}
