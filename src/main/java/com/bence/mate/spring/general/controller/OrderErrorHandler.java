package com.bence.mate.spring.general.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.bence.mate.spring.general.entity.Error;

import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class OrderErrorHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleException(RuntimeException ex){
        return ResponseEntity.badRequest().body(Error.builder().message(ex.getMessage()).build());
    }
}
