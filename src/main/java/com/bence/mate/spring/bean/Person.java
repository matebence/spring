package com.bence.mate.spring.bean;

import org.springframework.stereotype.Component;

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
