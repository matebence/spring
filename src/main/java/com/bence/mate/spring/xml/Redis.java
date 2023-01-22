package com.bence.mate.spring.xml;

import com.bence.mate.spring.xml.repository.AbstractRepository;

public class Redis {

	private AbstractRepository abstractRepository;

	public Redis(AbstractRepository abstractRepository) {
		this.abstractRepository = abstractRepository;
	}

	public String find() {
		return this.abstractRepository.find();
	}
}
