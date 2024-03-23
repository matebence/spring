package com.bence.mate.spring;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EntityScan(basePackages = { "com.bence.mate.spring.entity" })
@EnableJpaRepositories(basePackages = { "com.bence.mate.spring.repository" })
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
