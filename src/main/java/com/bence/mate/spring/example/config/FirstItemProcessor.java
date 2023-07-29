package com.bence.mate.spring.example.config;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FirstItemProcessor implements ItemProcessor<Integer, Long> {

	@Override
	public Long process(Integer item) throws Exception {
		log.info("Inside Item Processor");
		return Long.valueOf(item + 20);
	}
}
