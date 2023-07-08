package com.bence.mate.spring;

import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EntityScan("com.bence.mate.spring.entity")
@EnableR2dbcRepositories("com.bence.mate.spring.repository")
@ComponentScan({"com.bence.mate.spring.component", "com.bence.mate.spring.config", "com.bence.mate.spring.controller", "com.bence.mate.spring.service"})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
