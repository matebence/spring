package com.bence.mate.spring.app;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.SpringApplication;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@ComponentScan(value = { "com.bence.mate.spring" })
@EntityScan(value = { "com.bence.mate.spring.entity" })
@EnableJpaRepositories(value = { "com.bence.mate.spring.repository" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
