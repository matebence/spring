package com.bence.mate.spring.xml;

import com.bence.mate.spring.xml.repository.AbstractRepository;

public class Mongo {

	private AbstractRepository abstractRepository;

	public Mongo() {
	}

	public String find() {
		return this.abstractRepository.find();
	}

	public AbstractRepository getAbstractRepository() {
		return abstractRepository;
	}

	public void setAbstractRepository(AbstractRepository abstractRepository) {
		this.abstractRepository = abstractRepository;
	}
}