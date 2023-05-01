package com.bence.mate.spring.resource;

import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.http.ResponseEntity;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.entity.Person;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureJsonTesters
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PersonResourceIntegrationTest {

	@Autowired
	private JacksonTester<Person> json;

	@Autowired
	private PersonResource personResource;

	@Test
	public void whenPersonIsSaved_thenStatusCreatedIsExpected() throws Exception {
		// given
		Person instance = Person.builder().firstName("John").lastName("Doe").age(30).build();

		// when
		ResponseEntity<Person> response = personResource.save(instance);
		JsonContent<Person> result = json.write(response.getBody());

		// then
		assertThat(result).hasJsonPathNumberValue("$.id");
		assertThat(result).extractingJsonPathStringValue("$.firstName").isEqualTo("John");
		assertThat(result).extractingJsonPathStringValue("$.lastName").isEqualTo("Doe");
		assertThat(result).extractingJsonPathNumberValue("$.age").isEqualTo(30);
	}
}
