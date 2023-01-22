package com.bence.mate.spring.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
public class VehicleService {

	private static Logger logger = LoggerFactory.getLogger(VehicleService.class);

	@Lazy
	@Autowired
	@Qualifier("Mercedes")
	private Vehicle vehicle;

	@PostConstruct
	public void postConstruct() {
		logger.info("{}", vehicle.getName());
		logger.info("{}", vehicle.getPerson().getName());
	}
}
