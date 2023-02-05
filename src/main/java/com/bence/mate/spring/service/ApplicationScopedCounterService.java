package com.bence.mate.spring.service;

import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@ApplicationScope
// @Scope(value = WebApplicationContext.SCOPE_APPLICATION)
public class ApplicationScopedCounterService {

	private int counter = 0;

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}
}
