package com.bence.mate.spring.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

// this will be injected if spring.profiles.active is equal to dev
@Profile("dev")
@Component(value = "personComponent")
public class Person {

	private String name;

	public Person() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
