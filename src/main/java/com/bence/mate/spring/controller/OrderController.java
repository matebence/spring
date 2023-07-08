package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.MediaType;

import com.bence.mate.spring.entity.Order;

import java.util.stream.Collectors;
import java.util.Random;
import java.util.List;

import java.time.LocalDateTime;
import java.time.Duration;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@GetMapping(value = "/getAll", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Order> getAll() {
		return Flux.just(
				new Order(1L, 100.0, LocalDateTime.now()),
		        new Order(2L, 200.0, LocalDateTime.now()),
		        new Order(3L, 300.0, LocalDateTime.now()),
		        new Order(4L, 100.0, LocalDateTime.now()),
		        new Order(5L, 200.0, LocalDateTime.now()),
		        new Order(6L, 300.0, LocalDateTime.now()),
		        new Order(7L, 100.0, LocalDateTime.now()),
		        new Order(8L, 200.0, LocalDateTime.now()),
		        new Order(9L, 300.0, LocalDateTime.now()),
		        new Order(10L, 100.0, LocalDateTime.now()),
		        new Order(11L, 200.0, LocalDateTime.now()),
		        new Order(12L, 300.0, LocalDateTime.now()),
		        new Order(13L, 200.0, LocalDateTime.now()),
		        new Order(14L, 300.0, LocalDateTime.now()),
		        new Order(15L, 100.0, LocalDateTime.now()),
		        new Order(16L, 200.0, LocalDateTime.now()),
		        new Order(17L, 300.0, LocalDateTime.now()),
		        new Order(18L, 100.0, LocalDateTime.now()),
		        new Order(19L, 200.0, LocalDateTime.now()),
		        new Order(20L, 300.0, LocalDateTime.now())
		).delayElements(Duration.ofSeconds(1)).log();
	}
	
	@GetMapping(value = "/traditional/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Order> getAllTraditional() {
		if (new Random().nextInt(50) > 30) throw new RuntimeException("The value is greater then 30");
		
		return List.of(     
				new Order(1L, 100.0, LocalDateTime.now()),
		        new Order(2L, 200.0, LocalDateTime.now()),
		        new Order(3L, 300.0, LocalDateTime.now()),
		        new Order(4L, 100.0, LocalDateTime.now()),
		        new Order(5L, 200.0, LocalDateTime.now()),
		        new Order(6L, 300.0, LocalDateTime.now()),
		        new Order(7L, 100.0, LocalDateTime.now()),
		        new Order(8L, 200.0, LocalDateTime.now()),
		        new Order(9L, 300.0, LocalDateTime.now()),
		        new Order(10L, 100.0, LocalDateTime.now()),
		        new Order(11L, 200.0, LocalDateTime.now()),
		        new Order(12L, 300.0, LocalDateTime.now()),
		        new Order(13L, 200.0, LocalDateTime.now()),
		        new Order(14L, 300.0, LocalDateTime.now()),
		        new Order(15L, 100.0, LocalDateTime.now()),
		        new Order(16L, 200.0, LocalDateTime.now()),
		        new Order(17L, 300.0, LocalDateTime.now()),
		        new Order(18L, 100.0, LocalDateTime.now()),
		        new Order(19L, 200.0, LocalDateTime.now()),
		        new Order(20L, 300.0, LocalDateTime.now())
		).stream().peek(t -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				log.info("{}", e);
			}
		}).collect(Collectors.toList());
	}
	
	/*
	 * Using ServerSentEvent for meta values likes event, data & id
	@GetMapping("/stream-sse")
	public Flux<ServerSentEvent<String>> streamEvents() {
	    return Flux.interval(Duration.ofSeconds(1))
	      .map(sequence -> ServerSentEvent.<String> builder()
	        .id(String.valueOf(sequence))
	          .event("periodic-event")
	          .data("SSE - " + LocalTime.now().toString())
	          .build());
	}
	
	* Using tradational SseEmitter from Spring MVC (deperecated)
	@GetMapping("/stream-sse-mvc")
	public SseEmitter streamSseMvc() {
	    SseEmitter emitter = new SseEmitter();
	    ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
	    sseMvcExecutor.execute(() -> {
	        try {
	            for (int i = 0; true; i++) {
	                SseEventBuilder event = SseEmitter.event()
	                  .data("SSE MVC - " + LocalTime.now().toString())
	                  .id(String.valueOf(i))
	                  .name("sse event - mvc");
	                emitter.send(event);
	                Thread.sleep(1000);
	            }
	        } catch (Exception ex) {
	            emitter.completeWithError(ex);
	        }
	    });
	    return emitter;
	}
	*/
}
