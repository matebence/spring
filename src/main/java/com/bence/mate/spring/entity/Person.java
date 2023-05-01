package com.bence.mate.spring.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person")
public class Person {

	@Id
	@Getter
	@Setter
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Getter
	@Setter
	@Column(name = "first_name")
	private String firstName;

	@Getter
	@Setter
	@Column(name = "last_name")
	private String lastName;

	@Getter
	@Setter
	@Column(name = "age")
	private Integer age;

	public Person update(Person person) {
		this.firstName = person.getFirstName();
		this.lastName = person.getLastName();
		this.age = person.getAge();
		return this;
	}
}
