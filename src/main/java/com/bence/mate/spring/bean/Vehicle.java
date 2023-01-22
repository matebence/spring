package com.bence.mate.spring.bean;

import org.springframework.stereotype.Component;

@Component(value = "vehicleComponent")
public class Vehicle {

	private String name;

	private Person person;

	public Vehicle() {
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
