package com.bence.mate.spring.controller;

import com.bence.mate.spring.service.producer.LibraryEventProducer;
import com.bence.mate.spring.entity.LibraryEventType;
import com.bence.mate.spring.entity.LibraryEvent;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import lombok.extern.slf4j.Slf4j;
import jakarta.validation.Valid;

@Slf4j
@RestController
public class LibraryEventsController {

	@Autowired
	private LibraryEventProducer libraryEventProducer;

	@PostMapping("/v1/libraryevent")
	public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent) throws JsonProcessingException {
		// this is async so we first return with 200 and then we get the result from Kafka
		libraryEvent.setLibraryEventType(LibraryEventType.NEW);
		
		if (libraryEvent.getBook().getBookName().contains("A")) {
			libraryEventProducer.sendLibraryEventAsync(libraryEvent);			
		} else {
			libraryEventProducer.sendLibraryEventWithHeaderAsync(libraryEvent);						
		}

		return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
	}

	@PutMapping("/v1/libraryevent")
	public ResponseEntity<?> putLibraryEvent(@RequestBody @Valid LibraryEvent libraryEvent)	throws JsonProcessingException, ExecutionException, InterruptedException, TimeoutException {
		log.info("LibraryEvent : {} ", libraryEvent);

		if (libraryEvent.getLibraryEventId() == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the LibraryEventId");
		}

		// this is sync and it will be blocked	
		libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
		libraryEventProducer.sendLibraryEventSync(libraryEvent);
		
		return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
	}
}
