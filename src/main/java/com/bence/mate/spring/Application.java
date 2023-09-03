package com.bence.mate.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

import org.springframework.kafka.config.TopicBuilder;
import org.springframework.context.annotation.Bean;
import org.apache.kafka.clients.admin.NewTopic;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// First admin section has be set in application.yml
	// Second only for dev usage
	@Bean
    public NewTopic libraryEvents(){
        return TopicBuilder.name("test-events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
