package com.bence.mate.spring;

import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
@EnableWebSocketMessageBroker
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
