package com.bence.mate.author;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.SpringApplication;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@ComponentScan(basePackages = { "com.bence.mate.author" })
@EntityScan(basePackages = { "com.bence.mate.author.entity" })
@EnableJpaRepositories(basePackages = { "com.bence.mate.author.repository" })
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
