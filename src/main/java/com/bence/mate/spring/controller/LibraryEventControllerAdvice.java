package com.bence.mate.spring.controller;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.validation.FieldError;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.stream.Collectors;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class LibraryEventControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleRequestBody(MethodArgumentNotValidException ex) {
        List<FieldError> errorList = ex.getBindingResult().getFieldErrors();
        String errorMessage = errorList.stream()
                .map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage())
                .sorted()
                .collect(Collectors.joining(", "));
        
        log.info("errorMessage : {} ", errorMessage);
        
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }
}
