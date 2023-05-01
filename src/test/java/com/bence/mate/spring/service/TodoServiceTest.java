package com.bence.mate.spring.service;

import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bence.mate.spring.data.Task;
import com.bence.mate.spring.data.Todo;

import java.util.List;

import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestClientTest(TodoService.class)
// A bean that doesn't use RestTemplateBuilder but instead injects a RestTemplate directly, then we use:
@AutoConfigureWebClient(registerRestTemplate = true)
public class TodoServiceTest {

	@Autowired
	private TodoService client;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private MockRestServiceServer server;

	@Test
	public void whenCallingGetTodos_thenClientMakesCorrectCall() throws Exception {
		// given
		Todo todo = Todo.builder().total(1).skip(0).limit(1).todos(List.of(new Task(1L, "Test", true, 233L))).build();
		String todoJson = objectMapper.writeValueAsString(todo);
		server.expect(requestTo(TodoService.TODO_URL)).andRespond(withSuccess(todoJson, MediaType.APPLICATION_JSON));

		// when
		Todo instance = client.get();

		// then
		assertEquals(instance.getSkip(), 0);
		assertEquals(instance.getLimit(), 1);
		assertEquals(instance.getTotal(), 1);
		assertFalse(instance.getTodos().isEmpty());

		for (Task task : instance.getTodos()) {
			assertEquals(task.getId(), 1L);
			assertTrue(task.getCompleted());
			assertEquals(task.getUserId(), 233L);
			assertEquals(task.getTodo(), "Test");
		}
	}
}
