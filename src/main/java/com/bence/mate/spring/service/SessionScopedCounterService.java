package com.bence.mate.spring.service;

import org.springframework.web.context.annotation.SessionScope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@SessionScope
// @Scope(value = WebApplicationContext.SCOPE_SESSION)
public class SessionScopedCounterService {

	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}
