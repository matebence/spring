package com.bence.mate.spring.component;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.BodyInserters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.MediaType;

import com.bence.mate.spring.service.OrderService;
import com.bence.mate.spring.entity.Order;

import reactor.core.publisher.Mono;
import java.time.Duration;

@Component
public class OrderHandler {
	
	@Autowired
	private OrderService orderService;

	public Mono<ServerResponse> getAllOrders(ServerRequest serverRequest) {
		/* 
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(orderService.getAllOrders(), Order.class);
		*/

		return ServerResponse.ok()
		        .contentType(MediaType.TEXT_EVENT_STREAM)
		        .body(BodyInserters.fromPublisher(orderService.getAllOrders().delayElements(Duration.ofSeconds(1)), Order.class));
	}
	
	public Mono<ServerResponse> getOrderById(ServerRequest serverRequest) {
		Long id = Long.valueOf(serverRequest.queryParam("id").get());
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(orderService.getOrderById(id), Order.class);
	}
	
	public Mono<ServerResponse> createOrder(ServerRequest serverRequest) {
		Mono<Order> newOrder = serverRequest.bodyToMono(Order.class);
		
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(newOrder.flatMap(t -> orderService.saveOrder(t)), Order.class);
	}
	
	public Mono<ServerResponse> updateOrder(ServerRequest serverRequest) {
		Long id = Long.valueOf(serverRequest.pathVariable("id"));
		
		/* 
 		Mono<Order> upadteOrder = serverRequest.bodyToMono(Order.class);
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(orderService.getOrderById(id)
						.flatMap(db -> upadteOrder.map(rest -> {
							db.setAmount(rest.getAmount());
							db.setPlacedDate(rest.getPlacedDate());
							return db;
						})), Order.class);
		*/
		
         return Mono
                .zip(
                    (data) -> {
                        Order db = (Order) data[0];
                        Order rest = (Order) data[1];
                        db.setAmount(rest.getAmount());
                        db.setPlacedDate(rest.getPlacedDate());
                        return db;
                    },
                    orderService.getOrderById(id),
                    serverRequest.bodyToMono(Order.class)
                )
                .cast(Order.class)
                .flatMap(order -> orderService.saveOrder(order))
                .flatMap(order -> ServerResponse.noContent().build());
	}
	
	public Mono<ServerResponse> deleteOrder(ServerRequest serverRequest) {
		Long id = Long.valueOf(serverRequest.headers().firstHeader("id"));

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(orderService.deleteOrder(id), Void.class);
	}
}
