package com.bence.mate.spring;

import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.Consumer;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import org.springframework.test.context.TestPropertySource;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.entity.LibraryEvent;
import com.bence.mate.spring.entity.Book;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Run the app on a custom port
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) 
// Use embedded Kafka instead of the real one
@EmbeddedKafka(topics = { "library-events" }, partitions = 3) 
// Override embbeded Kafka properties
@TestPropertySource(properties = {
		"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"
})
public class LibraryEventsControllerIntegrationTest {

	private Consumer<Integer, String> consumer;

	// We could inject here the LibraryEventsController too
	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@BeforeEach
	void setUp() {
		Map<String, Object> configs = new HashMap<>(KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker));
		configs.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		consumer = new DefaultKafkaConsumerFactory<>(configs, new IntegerDeserializer(), new StringDeserializer()).createConsumer();
		embeddedKafkaBroker.consumeFromAllEmbeddedTopics(consumer);
	}

	@AfterEach
	void tearDown() {
		consumer.close();
	}

	@Test
	@Timeout(5)
	void postLibraryEvent() throws InterruptedException {
		// given
		Book book = Book.builder().bookId(123).bookAuthor("Dilip").bookName("Kafka using Spring Boot").build();

		LibraryEvent libraryEvent = LibraryEvent.builder().libraryEventId(null).book(book).build();
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

		// when
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.POST,
				request, LibraryEvent.class);

		// then
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());

		ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
		// Thread.sleep(3000); // we use JUnit5 @Timeout annotation instead
		assert consumerRecords.count() == 1;
		consumerRecords.forEach(record -> {
			String expectedRecord = "{\"libraryEventId\":null,\"libraryEventType\":\"NEW\",\"book\":{\"bookId\":123,\"bookName\":\"Kafka using Spring Boot\",\"bookAuthor\":\"Dilip\"}}";
			String value = record.value();
			assertEquals(expectedRecord, value);
		});
	}

	@Test
	@Timeout(5)
	void putLibraryEvent() throws InterruptedException {
		// given
		Book book = Book.builder().bookId(456).bookAuthor("Dilip").bookName("Kafka using Spring Boot").build();

		LibraryEvent libraryEvent = LibraryEvent.builder().libraryEventId(123).book(book).build();
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		HttpEntity<LibraryEvent> request = new HttpEntity<>(libraryEvent, headers);

		// when
		ResponseEntity<LibraryEvent> responseEntity = restTemplate.exchange("/v1/libraryevent", HttpMethod.PUT, request,
				LibraryEvent.class);

		// then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

		ConsumerRecords<Integer, String> consumerRecords = KafkaTestUtils.getRecords(consumer);
		// Thread.sleep(3000);
		assert consumerRecords.count() == 2;
		consumerRecords.forEach(record -> {
			if (record.key() != null) {
				String expectedRecord = "{\"libraryEventId\":123,\"libraryEventType\":\"UPDATE\",\"book\":{\"bookId\":456,\"bookName\":\"Kafka using Spring Boot\",\"bookAuthor\":\"Dilip\"}}";
				String value = record.value();
				assertEquals(expectedRecord, value);
			}
		});
	}
}
