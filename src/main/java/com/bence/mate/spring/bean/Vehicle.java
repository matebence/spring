package com.bence.mate.spring.bean;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//this will be injected if spring.profiles.active is equal to prod
@Profile("prod")
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
