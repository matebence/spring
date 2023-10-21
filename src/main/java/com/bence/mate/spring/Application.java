package com.bence.mate.spring;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import org.keycloak.adapters.springsecurity.authentication.KeycloakLogoutHandler;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.keycloak.adapters.AdapterDeploymentContext;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.SpringApplication;


@EnableWebSecurity
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public KeycloakConfigResolver keycloakConfigResolver() {
		// It will use application.properties instead of WEB-INF/keycloack.json
		return new KeycloakSpringBootConfigResolver();
	}

	@Bean
	public KeycloakLogoutHandler keycloakLogoutHandler(KeycloakConfigResolver keycloakConfigResolver) {
		// For complete logout
		return new KeycloakLogoutHandler(new AdapterDeploymentContext(keycloakConfigResolver));
	}
}
