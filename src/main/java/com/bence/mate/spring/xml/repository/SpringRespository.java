package com.bence.mate.spring.xml.repository;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class SpringRespository implements AbstractRepository {

	private static Logger logger = LoggerFactory.getLogger(SpringRespository.class);

	private int value;

	public SpringRespository() {
	}

	public SpringRespository(int value) {
		this.value = value;
		logger.info("{}", this.value);
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public String find() {
		return "Hello World";
	}
}
