package com.bence.mate.spring.sort;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BinarySearchImpl {

	@Autowired
	@Qualifier("bubble")
	private SortAlgorithm sortAlgorithm;

	public int binarySearch(int[] numbers, int numberToSearchFor) {
		int[] sortedNumbers = sortAlgorithm.sort(numbers);
		log.info("{}", sortAlgorithm);
		return 3;
	}

	@PostConstruct
	public void postConstruct() {
		log.info("postConstruct");
	}

	@PreDestroy
	public void preDestroy() {
		log.info("preDestroy");
	}
}
