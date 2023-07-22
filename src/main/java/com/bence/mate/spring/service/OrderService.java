package com.bence.mate.spring.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.repository.OrderRepository;
import com.bence.mate.spring.dto.OrderCompletedEvent;
import com.bence.mate.spring.entity.OrderStatus;
import com.bence.mate.spring.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Transactional
	public Order placeOrder(Order order) {
		log.info("Placing and order {}", order);
		order.setStatus(OrderStatus.COMPLETED);
		Order instace = orderRepository.save(order);

		log.info("Publishing order completed event");
		publisher.publishEvent(new OrderCompletedEvent(order));
		
		return instace;
	}
}
