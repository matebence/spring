package com.bence.mate.spring.project.rest.reader;

import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;

@Component
public class JsonReader {

	public JsonItemReader<Student> read() throws FileNotFoundException {
		JsonItemReader<Student> jsonItemReader = new JsonItemReader<>();
		
		jsonItemReader.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:input.json")));
		jsonItemReader.setJsonObjectReader(new JacksonJsonObjectReader<>(Student.class));
		
		jsonItemReader.setMaxItemCount(8); // end at 8
		jsonItemReader.setCurrentItemCount(2); //start from 2
		
		return jsonItemReader;
	}
}
