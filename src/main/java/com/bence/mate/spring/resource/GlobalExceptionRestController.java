package com.bence.mate.spring.resource;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.bence.mate.spring.entity.Error;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
//We make sure that we catch only the controllers with @RestController annotation
public class GlobalExceptionRestController {

	@GetMapping
	@ExceptionHandler({ Exception.class })
	public ResponseEntity<Error> accountNotFoundExceptionHandler(Exception exception) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Error(exception.getMessage()));
	}
}
