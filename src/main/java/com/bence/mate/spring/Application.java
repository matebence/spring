package com.bence.mate.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
// @EnableBatchProcessing DEPRECATED VERSION
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
