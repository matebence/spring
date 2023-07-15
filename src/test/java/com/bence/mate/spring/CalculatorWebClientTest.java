package com.bence.mate.spring;

import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.junit.jupiter.api.Test;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class CalculatorWebClientTest {

	@Autowired
	private WebClient webClient;

	@Test
	public void simpleGetCallTest() {
		Mono<Integer> result = webClient.get()
				.uri("calculator/{a}/{b}", 5, 5)
				.header("OP", "*")
				.retrieve()
				.bodyToMono(Integer.class)
				.doOnNext(System.out::println)
				.doOnError(err -> System.out.println(err.getMessage()));

		StepVerifier.create(result)
				.expectNext(25)
				// .assertNext(t -> assertEquals(25, t))
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	public void simplePostCallTest() {
		Mono<Integer> result = webClient.post()
				.uri("calculator/{a}/{b}", 5, 5)
				.header("OP", "*")
				.bodyValue(Map.of("key", "value"))
				.retrieve()
				.bodyToMono(Integer.class)
				.doOnError(err -> System.out.println(err.getMessage()));

		StepVerifier.create(result)
				.verifyError(WebClientResponseException.NotFound.class);
	}

	@Test
	public void simpleGetCallTestWithExchange() {
		Mono<Integer> result = webClient.get()
				.uri("calculator/{a}/{b}", 5, 5)
				.header("OP", "*")
				.exchangeToMono(t -> {
					if (t.statusCode().equals(HttpStatusCode.valueOf(200))) {
						return Mono.just(-1);
					}
					return t.bodyToMono(Integer.class);
				}).doOnNext(System.out::println).doOnError(err -> System.out.println(err.getMessage()));

		StepVerifier.create(result)
				.expectNextMatches(t -> t == -1)
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	public void simpleErrorTest() {
		Mono<Integer> result = webClient.get()
				.uri("calculator/{a}/{b}", 5, 5)
				.retrieve().bodyToMono(Integer.class)
				.doOnError(err -> System.out.println(err.getMessage()));

		StepVerifier.create(result)
				.verifyError(WebClientResponseException.BadRequest.class);
	}
}
