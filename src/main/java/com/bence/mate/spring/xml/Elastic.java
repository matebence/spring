package com.bence.mate.spring.xml;

import com.bence.mate.spring.xml.repository.AbstractRepository;

public class Elastic {

	private AbstractRepository abstractRepository;

	public Elastic() {
	}

	public String find() {
		return this.abstractRepository.find();
	}

	public AbstractRepository getabstractRepository() {
		return abstractRepository;
	}

	public void setabstractRepository(AbstractRepository abstractRepository) {
		this.abstractRepository = abstractRepository;
	}
}
