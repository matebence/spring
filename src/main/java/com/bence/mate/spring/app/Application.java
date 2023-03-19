package com.bence.mate.spring.app;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.boot.SpringApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@EnableCaching
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.bence.mate.spring" })
@EntityScan(basePackages = { "com.bence.mate.spring.entity" })
@EnableJpaRepositories(basePackages = { "com.bence.mate.spring.repository" })
@OpenAPIDefinition(info = @Info(title = "Test API", version = "1.0", description = "A simple Budget API for testing"))
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
