package com.bence.mate.spring.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.service.OrderService;
import com.bence.mate.spring.entity.Order;

@RestController
@RequestMapping(path = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderResource {

	@Autowired
	private OrderService orderService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Order> placeOrder(@RequestBody Order order) {
		Order instance = orderService.placeOrder(order);

		return ResponseEntity.status(HttpStatus.CREATED).body(instance);
	}
}
