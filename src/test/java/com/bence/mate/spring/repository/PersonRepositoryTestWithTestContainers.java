package com.bence.mate.spring.repository;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.AbstractContainerBaseTest;
import com.bence.mate.spring.entity.Person;

import java.util.NoSuchElementException;
import java.util.Optional;

import java.sql.SQLException;
import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class PersonRepositoryTestWithTestContainers extends AbstractContainerBaseTest {

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private DataSource dataSource;

	@BeforeEach
	void setup() throws SQLException {
		log.info(MY_SQL_CONTAINER.getUsername());
		log.info(MY_SQL_CONTAINER.getPassword());
		log.info(MY_SQL_CONTAINER.getDatabaseName());
		log.info(MY_SQL_CONTAINER.getJdbcUrl());

		log.info(dataSource.getConnection().getMetaData().getURL().toString());
	}

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

	@Test
	@Sql({ "/insert.sql" })
	public void whenSqlScriptIsExecuted_thenFooShouldExist() {
		// given
		// when
		Optional<Person> actual = personRepository.findById(99L);

		// then
		assertAll(() -> {
			// we use all because otherwise we don't know which of them failed
			assertNotNull(actual.get());
			assertEquals(99L, actual.get().getId());
			assertEquals(30, actual.get().getAge());
			assertEquals("Baz", actual.get().getLastName());
			assertEquals("Foo", actual.get().getFirstName());
		});
	}
}
