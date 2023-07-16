package com.bence.mate.spring.project.order.component;

import com.bence.mate.spring.project.order.service.OrderFulfillmentService;
import com.bence.mate.spring.project.order.dto.PurchaseOrderResponseDto;
import com.bence.mate.spring.project.order.dto.PurchaseOrderRequestDto;
import com.bence.mate.spring.project.order.service.OrderQueryService;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import reactor.core.publisher.Mono;

@Component
public class PurchaseOrderHandler {

	@Autowired
	private OrderQueryService queryService;

	@Autowired
	private OrderFulfillmentService orderFulfillmentService;

	public Mono<ServerResponse> getOrdersByUserId(ServerRequest serverRequest) {
		Integer id = Integer.valueOf(serverRequest.pathVariable("id"));

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(queryService.getProductsByUserId(id), PurchaseOrderResponseDto.class);
	}

	public Mono<ServerResponse> order(ServerRequest serverRequest) {
		Mono<PurchaseOrderRequestDto> requestDtoMono = serverRequest.bodyToMono(PurchaseOrderRequestDto.class);

		return ServerResponse.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(orderFulfillmentService.processOrder(requestDtoMono), PurchaseOrderResponseDto.class);
	}
}
