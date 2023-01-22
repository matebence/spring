package com.bence.mate.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = "com.bence.mate.spring")
// If the classes MathService, SearchService would exists out of the package where 
// the Application class exists 'com.bence.mate.spring' we would have to define the
// @ComponentScan annotation
public class Application {

	public static void main(String[] args) {
		// Using Spring with SpringBoots
		SpringApplication.run(Application.class, args);
	}
}
