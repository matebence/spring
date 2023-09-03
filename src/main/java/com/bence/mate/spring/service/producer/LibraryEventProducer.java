package com.bence.mate.spring.service.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.bence.mate.spring.entity.LibraryEvent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LibraryEventProducer {

	private final static String TOPIC = "library-events";

	@Autowired
	private KafkaTemplate<Integer, String> kafkaTemplate;

	@Autowired
	private ObjectMapper objectMapper;

	public void sendLibraryEventAsync(LibraryEvent libraryEvent) throws JsonProcessingException {
		String value = objectMapper.writeValueAsString(libraryEvent);
		Integer key = libraryEvent.getLibraryEventId();

		CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.sendDefault(key, value);
		completableFuture.whenComplete((success, failure) -> {
			if (failure != null) {
                log.info(failure.getMessage());
            }
			log.info("{}", success.getProducerRecord().key());
            log.info("{}", success.getProducerRecord().value());
		});
	}

	public CompletableFuture<SendResult<Integer, String>> sendLibraryEventWithHeaderAsync(LibraryEvent libraryEvent) throws JsonProcessingException {
		String value = objectMapper.writeValueAsString(libraryEvent);
		Integer key = libraryEvent.getLibraryEventId();

		List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
		ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(TOPIC, null, key, value, recordHeaders);

		CompletableFuture<SendResult<Integer, String>> completableFuture = kafkaTemplate.send(producerRecord);
		completableFuture.whenComplete((success, failure) -> {
			if (failure != null) {
                log.info(failure.getMessage());
            }
			log.info("{}", success.getProducerRecord().key());
            log.info("{}", success.getProducerRecord().value());
		});

		return completableFuture;
	}

	public SendResult<Integer, String> sendLibraryEventSync(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
		String value = objectMapper.writeValueAsString(libraryEvent);
		Integer key = libraryEvent.getLibraryEventId();
		SendResult<Integer, String> sendResult = null;

		try {
			sendResult = kafkaTemplate.sendDefault(key, value).get(1, TimeUnit.SECONDS);
		} catch (ExecutionException | InterruptedException e) {
			log.error("ExecutionException/InterruptedException Sending the Message and the exception is {}", e.getMessage());
			throw e;
		} catch (Exception e) {
			log.error("Exception Sending the Message and the exception is {}", e.getMessage());
			throw e;
		}

		return sendResult;
	}
}