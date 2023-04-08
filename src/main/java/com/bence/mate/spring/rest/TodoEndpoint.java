package com.bence.mate.spring.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.cloud.openfeign.FeignClient;

import com.bence.mate.spring.rest.data.Todo;
import com.bence.mate.spring.config.Feign;

import feign.Headers;

@FeignClient(name = "todos", url = "https://dummyjson.com/todos", configuration = Feign.class)
public interface TodoEndpoint {

	@Headers(value = "Content-Type: application/json")
	@RequestMapping(method = RequestMethod.GET)
	public Todo getTodos();

	@Headers(value = "Content-Type: application/json")
	@GetMapping(value = "/{id}")
	public Todo getTodoById(@PathVariable(value = "id") Long id);
}
