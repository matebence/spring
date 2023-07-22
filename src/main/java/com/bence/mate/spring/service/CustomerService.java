package com.bence.mate.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.repository.CustomerRepository;
import com.bence.mate.spring.dto.CustomerRegisteredEvent;
import com.bence.mate.spring.dto.CustomerRemovedEvent;
import com.bence.mate.spring.entity.Customer;

import java.util.Optional;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ApplicationEventPublisher publisher;

	public Customer register(Customer customer) {
		Customer instance = customerRepository.save(customer);
		publisher.publishEvent(new CustomerRegisteredEvent(customer));
		
		return instance;
	}
	
	public Optional<Customer> find(Long id) {
		return customerRepository.findById(id);
	}

	public void remove(Customer customer) {
		customerRepository.delete(customer);
		publisher.publishEvent(new CustomerRemovedEvent(customer));
	}
}
