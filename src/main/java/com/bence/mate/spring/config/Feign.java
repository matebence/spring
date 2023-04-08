package com.bence.mate.spring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class Feign {

    @Bean
    public BasicAuthRequestInterceptor getBasicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}
