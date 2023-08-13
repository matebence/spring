package com.bence.mate.spring;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.boot.test.context.SpringBootTest;

import com.bence.mate.spring.dto.ChatOutMessage;
import com.bence.mate.spring.dto.ChatInMessage;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.lang.reflect.Type;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static java.util.concurrent.TimeUnit.SECONDS;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatServerIntegrationTests {

	@LocalServerPort
	private int port;

	private WebSocketStompClient webSocketStompClient;

	@BeforeEach
	public void setup() {
		this.webSocketStompClient = new WebSocketStompClient(new SockJsClient(List.of(new WebSocketTransport(new StandardWebSocketClient()))));
	}

	@Test
	public void verifyGreetingIsReceived() throws Exception {
		String endpoint = String.format("ws://localhost:%d/landon-stomp-chat", port);

		final CountDownLatch latch = new CountDownLatch(1);
		BlockingQueue<ChatOutMessage> blockingQueue = new ArrayBlockingQueue<>(1);

		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession session = webSocketStompClient.connectAsync(endpoint, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		session.subscribe("/topic/guestchats", new StompFrameHandler() {

			@Override
			public Type getPayloadType(StompHeaders headers) {
				return ChatInMessage.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				blockingQueue.add((ChatOutMessage) payload);
			}
		});

		ChatInMessage myMessage = new ChatInMessage("Hello Spring");
		session.send("/app/guestchat", myMessage);

		if (latch.await(3, TimeUnit.SECONDS)) {
			assertEquals("Hello Spring", blockingQueue.poll().getContent());
		}
	}

	@Test
	void verifyWelcomeMessageIsSent() throws Exception {
		String endpoint = String.format("ws://localhost:%d/landon-stomp-chat", port);
		CountDownLatch latch = new CountDownLatch(1);

		webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
		StompSession session = webSocketStompClient.connectAsync(endpoint, new StompSessionHandlerAdapter() {}).get(1, SECONDS);

		session.subscribe("/topic/guestchats", new StompFrameHandler() {

			@Override
			public Type getPayloadType(StompHeaders headers) {
				return ChatInMessage.class;
			}

			@Override
			public void handleFrame(StompHeaders headers, Object payload) {
				latch.countDown();
			}
		});

		if (latch.await(3, TimeUnit.SECONDS)) {
			assertEquals(0, latch.getCount());
		}
	}
}