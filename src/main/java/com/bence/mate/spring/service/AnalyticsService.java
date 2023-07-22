package com.bence.mate.spring.service;

import org.springframework.stereotype.Service;

import com.bence.mate.spring.entity.Customer;

import lombok.extern.slf4j.Slf4j;
import lombok.SneakyThrows;

@Slf4j
@Service
public class AnalyticsService {

	@SneakyThrows
	public void registerNewCustomer(Customer customer) {
		log.info("calling analytics service for customer {}", customer);
		Thread.sleep(5000);
	}
}
