package com.bence.mate.spring.xml;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class IndexService implements IService {
	
	private static Logger logger = LoggerFactory.getLogger(IndexService.class); 

	private int value;
	
	public IndexService() {
	}
	
    public IndexService(int value) {
		this.value = value;
		logger.info("{}", this.value);
	}

	@Override
    public String serve() {
        return "Hello World";
    }

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}
