package com.bence.mate.spring.example.config;

import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Component
public class FirstItemReader implements ItemReader<Integer> {

	private List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
	private int i = 0;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("Inside Item Reader");
		Integer item;
		if (i < list.size()) {
			item = list.get(i);
			i++;
			return item;
		}
		i = 0;
		return null; // its the stop sign that there are no more items
	}
}
