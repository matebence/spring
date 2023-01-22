package com.bence.mate.spring.xml.factory;

import com.bence.mate.spring.xml.repository.AbstractRepository;
import com.bence.mate.spring.xml.repository.SpringRespository;

public class DatabaseFactory {

	public AbstractRepository getRepository(int number) {
		if (number == 1) {
			return new SpringRespository();
		}
		return null;
	}
}
