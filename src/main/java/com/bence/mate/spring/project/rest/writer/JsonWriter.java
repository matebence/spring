package com.bence.mate.spring.project.rest.writer;

import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;

@Component
public class JsonWriter {

	public JsonFileItemWriter<Student> write() throws FileNotFoundException {
		return new JsonFileItemWriter<>(new FileSystemResource(ResourceUtils.getFile("classpath:output.json")), new JacksonJsonObjectMarshaller<>());
	}
}
