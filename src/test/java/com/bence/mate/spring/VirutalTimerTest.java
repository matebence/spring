package com.bence.mate.spring;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import reactor.test.scheduler.VirtualTimeScheduler;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

@SpringBootTest
public class VirutalTimerTest {

	@Test
	void sampleFlux() {
		Flux<Integer> flux = Flux.range(1, 5)
				.delayElements(Duration.ofSeconds(1))
				.log();

		StepVerifier.withVirtualTime(() -> flux)
				.expectNext(1, 2, 3, 4, 5)
				.verifyComplete();
	}

	@Test
	void sampleFlux_WithVirtualTime() {
		VirtualTimeScheduler.getOrSet(); 				// ENABLING VIRTUAL TIME

		Flux<Integer> flux = Flux.range(1, 5)
				.delayElements(Duration.ofSeconds(1))
				.log();

		StepVerifier.withVirtualTime(() -> flux)
				.thenAwait(Duration.ofSeconds(10)) 		// SETTING MAX TIME FOR THE TEST COMPLETE
				.expectNext(1, 2, 3, 4, 5)
				.verifyComplete();
	}
}
