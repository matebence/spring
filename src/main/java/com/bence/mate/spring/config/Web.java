package com.bence.mate.spring.config;

import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class Web {

	@Bean
	public WebClient webClient() {
		return WebClient.builder().filter(ExchangeFilterFunctions.basicAuthentication("admin", "admin")).build();
	}
}
