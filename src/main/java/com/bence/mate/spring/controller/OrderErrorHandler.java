package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.http.ResponseEntity;
import com.bence.mate.spring.entity.Error;

@ControllerAdvice
public class OrderErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().body(Error.builder().message(ex.getMessage()).build());
    }
}
