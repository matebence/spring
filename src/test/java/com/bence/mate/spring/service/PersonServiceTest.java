package com.bence.mate.spring.service;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.bence.mate.spring.repository.PersonRepository;
import com.bence.mate.spring.entity.Person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PersonServiceTest {

	@InjectMocks
	private PersonService personService;

	@Mock
	private PersonRepository personRepository;

	@Test
	public void whenPersonIsSaved_thenIdShouldBeAssigned() {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		when(personRepository.save(any(Person.class))).thenReturn(person);

		// when
		Person instance = Person.builder().firstName("John").lastName("Doe").age(30).build();
		Person persisted = personService.save(instance);

		// then
		assertNull(instance.getId());
		assertEquals(person.getId(), persisted.getId());
		verify(personRepository, times(1)).save(any());		
	}
}
