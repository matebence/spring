package com.bence.mate.spring.resource;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Captor;

import java.util.Optional;

import com.bence.mate.spring.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bence.mate.spring.entity.Person;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebMvcTest(PersonResource.class)
public class PersonResourceTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean // it injeckts the PersonService into PersonResource, otherwise NoSuchBeanException
	private PersonService personService;

	@InjectMocks
	private PersonResource personResource;
	
	@Captor
	private ArgumentCaptor<Long> expectedId;

	@Test
	public void whenPersonIsSaved_thenStatusCreatedIsExpected() throws Exception {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		when(personService.save(any(Person.class))).thenReturn(person);

		// when
		Person instance = Person.builder().firstName("John").lastName("Doe").age(30).build();

		// then
		mockMvc.perform(post("/persons").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(instance)))
				.andExpect(status().isCreated())
				.andDo(print());
	}

	@Test
	public void whenPersonIsRetrived_thenStatusOkIsExpected() throws Exception {
		// given
		Person person = Person.builder().id(1L).firstName("John").lastName("Doe").age(30).build();
		when(personService.find(any(Long.class))).thenReturn(Optional.of(person));

		// when
		Long id = 1L;

		// then
		mockMvc.perform(get("/persons/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(person.getId()))
				.andExpect(jsonPath("$.age").value(person.getAge()))
				.andExpect(jsonPath("$.lastName").value(person.getLastName()))
				.andExpect(jsonPath("$.firstName").value(person.getFirstName()))
				.andDo(print());
		
		// for primitives use anyDouble(), anyBoolean()
		// use eq() to mix matchers and concrete values method(any(), eq(400.0))
		verify(personService, times(1)).find(anyLong());

	}

	@Test
	public void whenPersonIsUpdated_thenValueShouldBeChanged() throws Exception {
		// given
		Person instance = Person.builder().id(1L).firstName("Doe").lastName("John").age(60).build();
		when(personService.update(any(Person.class), any(Long.class))).thenReturn(instance);

		// when
		Long id = 1L;
		Person person = Person.builder().firstName("John").lastName("Doe").age(30).build();

		// then
		mockMvc.perform(put("/persons/{id}", id).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(person)))
				.andExpect(jsonPath("$.id").value(instance.getId()))
				.andExpect(jsonPath("$.age").value(instance.getAge()))
				.andExpect(jsonPath("$.lastName").value(instance.getLastName()))
				.andExpect(jsonPath("$.firstName").value(instance.getFirstName()))
				.andDo(print());
		verify(personService, times(1)).update(any(), eq(1L));
	}

	@Test
	public void whenPersonIsDeleted_thenStatusOkIsExpected() throws Exception {
		// given
		doNothing().when(personService).delete(any(Long.class));

		// when
		Long id = 1L;

		// then
		mockMvc.perform(delete("/persons/{id}", id).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent())
				.andDo(print());
		
		verify(personService, times(1)).delete(expectedId.capture());
		assertEquals(id, expectedId.getValue());
	}
}
