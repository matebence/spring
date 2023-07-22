package com.bence.mate.spring.service;

import org.springframework.stereotype.Service;

import com.bence.mate.spring.entity.Customer;
import com.bence.mate.spring.entity.Order;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService {
	public void sendRegisterEmail(Customer customer) {
		log.info("Sending registration email to customer {}", customer);
	}

	public void sendCustomerRemovedEmail(Customer customer) {
		log.info("Sending removed email for customer {}", customer);
	}

	public void sendOrderEmail(Order order) {
		log.info("Sending email for order {}", order);
	}
}
