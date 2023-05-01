package com.bence.mate.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import com.bence.mate.spring.data.Todo;

@Service
public class TodoService {

	public final static String TODO_URL = "https://dummyjson.com/todos";

	@Autowired
	private RestTemplate restTemplate;

	public Todo get() {
		ResponseEntity<Todo> responseEntity = restTemplate.exchange(TODO_URL, HttpMethod.GET, HttpEntity.EMPTY, Todo.class);
		return responseEntity.getBody();
	}
}
