package com.bence.mate.spring.project.rest.writer;

import com.bence.mate.spring.project.rest.dto.Student;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.batch.item.Chunk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SimpleWriter implements ItemWriter<Student> {

	@Override
	public void write(Chunk<? extends Student> chunk) throws Exception {
        log.info("Inside Item Writer");
        chunk.forEach(e -> log.info("{}", e));
	}
}
