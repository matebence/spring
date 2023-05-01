package com.bence.mate.spring.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.entity.Person;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

// It uses by default the H2 in memory database
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PersonRepositoryTest {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void whenPersonIsSaved_thenItShouldBePersisted() {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();

		// when
		Person persisted = personRepository.save(person);

		// then
		assertNotNull(persisted.getId());
	}

	@Test
	public void whenWeAreLookingForAPerson_thenOptionalValueShouldBeReturned() {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		Person persisted = personRepository.save(person);

		// when
		Optional<Person> actual = personRepository.findById(persisted.getId());

		// then
		assertNotNull(actual.get());
		assertEquals(persisted.getId(), actual.get().getId());
		assertEquals(persisted.getAge(), actual.get().getAge());
		assertEquals(persisted.getLastName(), actual.get().getLastName());
		assertEquals(persisted.getFirstName(), actual.get().getFirstName());
	}

	@Test
	public void whenDeleteIsCalled_thenPersonShouldNotExist() {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		Person persisted = personRepository.save(person);

		// when
		personRepository.delete(persisted);
		Optional<Person> actual = personRepository.findById(persisted.getId());
		Executable executable = () -> actual.get();

		// then
		assertFalse(actual.isPresent());
		assertThrows(NoSuchElementException.class, executable);
	}

	@Test
	public void whenUpdateIsCalled_thenValueShouldChange() {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		Person persisted = personRepository.save(person);

		// when
		persisted.setFirstName("Test");
		Person updated = personRepository.save(persisted);

		// then
		assertEquals("Test", updated.getFirstName());
	}
}
