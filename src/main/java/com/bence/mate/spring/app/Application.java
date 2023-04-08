package com.bence.mate.spring.app;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

import com.bence.mate.spring.rest.TodoEndpoint;
import com.bence.mate.spring.entity.Contact;
import com.bence.mate.spring.rest.data.Task;
import com.bence.mate.spring.rest.data.Todo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableCaching
@EnableAutoConfiguration
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@ComponentScan(basePackages = { "com.bence.mate.spring" })
@EntityScan(basePackages = { "com.bence.mate.spring.entity" })
@EnableFeignClients(basePackages = { "com.bence.mate.spring.rest" })
@EnableJpaRepositories(basePackages = { "com.bence.mate.spring.repository" })
@OpenAPIDefinition(info = @Info(title = "Test API", version = "1.0", description = "A simple Budget API for testing"))
public class Application implements CommandLineRunner {

	private final static String TODO_URL = "https://dummyjson.com/todos";

	@Autowired
	private WebClient webClient;

	@Autowired
	private TodoEndpoint todoEndpoint;

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("------------------FEIGN-------------------");
		Todo todo = todoEndpoint.getTodos();
		for (Task element : todo.getTodos()) {
			log.info(element.getTodo());
		}

		log.info("---------------RESTTEMPLATE---------------");
		HttpHeaders headers = new HttpHeaders();
		headers.add("invocationFrom", "RestTemplate");
		HttpEntity<Contact> httpEntity = new HttpEntity<>(headers);
		ResponseEntity<Todo> responseEntity = restTemplate.exchange(TODO_URL, HttpMethod.GET, httpEntity, Todo.class);

		todo = responseEntity.getBody();
		for (Task element : todo.getTodos()) {
			log.info(element.getTodo());
		}

		log.info("----------------WEBCLIENT-----------------");
        todo = webClient.get().uri(TODO_URL)
                .header("invocationFrom","WebClient")
                .retrieve()
                .bodyToMono(Todo.class).block();
        
		for (Task element : todo.getTodos()) {
			log.info(element.getTodo());
		}
	}
}
