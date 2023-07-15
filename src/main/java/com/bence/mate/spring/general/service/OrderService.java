package com.bence.mate.spring.general.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.general.repository.OrderRepository;
import com.bence.mate.spring.general.entity.Order;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	public Flux<Order> getAllOrders() {
		return orderRepository.findAll();
	}
	
	public Mono<Order> getOrderById(Long id){
		return orderRepository.findById(id);
	}
	
	public Mono<Order> saveOrder(Order order) {
		return orderRepository.save(order);
	}
	
	public Mono<Void> deleteOrder(Long id) {
		return orderRepository.deleteById(id);
	}
}
