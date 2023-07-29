package com.bence.mate.spring.example.config;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.Chunk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FirstItemWriter implements ItemWriter<Long> {

	@Override
	public void write(Chunk<? extends Long> chunk) throws Exception {
		log.info("Inside Item Writer");
		chunk.forEach(item -> log.info("{}", item));
	}
}
