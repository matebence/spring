package com.bence.mate.spring.coupling.spring.qualifier;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Component
@Scope(value = BeanDefinition.SCOPE_SINGLETON)
public class SearchService {

	private static Logger logger = LoggerFactory.getLogger(SearchService.class);

	@Autowired
	@Qualifier(value = "binary")
	private SearchAlgorithm searchAlgorithm;

	@PostConstruct
	public void postConstruct() {
		logger.info("postConstruct");
	}

	public boolean found(int[] haystack, int needle) {
		return searchAlgorithm.found(haystack, needle);
	}

	@PreDestroy
	public void preDestroy() {
		logger.info("preDestroy");
	}
}
