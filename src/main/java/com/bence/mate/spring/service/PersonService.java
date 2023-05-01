package com.bence.mate.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.repository.PersonRepository;
import com.bence.mate.spring.entity.Person;

import java.util.Optional;

@Service
public class PersonService {
	
	@Autowired
	private PersonRepository personRepository;
	
	public Person save(Person person) {
		return personRepository.save(person);
	}
	
	public Optional<Person> find(Long id) {
		return personRepository.findById(id);
	}
	
	public void delete(Long id) {
		Optional<Person> person = find(id);
		person.ifPresent(e -> personRepository.delete(e));
	}
	
	public Person update(Person person, Long id) {
		return find(id).map(e -> e.update(person)).orElseThrow(NullPointerException::new);
	}
}
