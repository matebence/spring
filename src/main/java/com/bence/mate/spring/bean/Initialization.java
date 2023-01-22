package com.bence.mate.spring.bean;

import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Bean;

@Configuration
public class Initialization {

	@Bean
	@Primary
	public Vehicle vehicle() {
		Vehicle vehicle = new Vehicle();
		vehicle.setName("Volkswagen");
		return vehicle;
	}

	@Bean
	public String hello() {
		return "Hello world";
	}

	@Bean
	public Integer number() {
		return 1996;
	}

	@Bean(name = "BMW")
	public Vehicle BMW() {
		Vehicle vehicle = new Vehicle();
		vehicle.setName("BMW");
		return vehicle;
	}

	// This would produce NoUniqueBeanDefinicationException, because there are
	// multiple vehicle instances with the same name
	// public Vehicle vehicle() {
	// Vehicle vehicle = new Vehicle();
	// vehicle.setName("Audi");
	// return vehicle;
	// }

	// To fix NoUniqueBeanDefinicationException we use the name attribute
	// The name here is the method name
	@Bean(name = "Audi")
	public Vehicle Audi() {
		Vehicle vehicle = new Vehicle();
		vehicle.setName("Audi");
		return vehicle;
	}

	@Bean
	public Person person() {
		Person person = new Person();
		person.setName("Alex");
		return person;
	}

	@Bean
	@Qualifier("Mercedes")
	// The upper person will be auto injected
	public Vehicle personVehicle(@Qualifier("person") Person person) {
		Vehicle vehicle = new Vehicle();
		vehicle.setPerson(person);
		vehicle.setName("Mercedes");
		return vehicle;
	}
}
