package com.bence.mate.spring.project.product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.bence.mate.spring.project.product.dto.ProductDto;

import reactor.core.publisher.Sinks;
import reactor.core.publisher.Flux;

@Configuration
public class SinkConfig {

	@Bean
	public Sinks.Many<ProductDto> sink() {
		return Sinks.many().replay().limit(1);
	}

	@Bean
	public Flux<ProductDto> productBroadcast(Sinks.Many<ProductDto> sink) {
		return sink.asFlux();
	}
}
