package com.bence.mate.spring.xml;

import com.bence.mate.spring.xml.repository.AbstractRepository;

public class MySQL {

	private AbstractRepository abstractRepository;

	public MySQL(AbstractRepository abstractRepository) {
		this.abstractRepository = abstractRepository;
	}

	public String find() {
		return this.abstractRepository.find();
	}
}
