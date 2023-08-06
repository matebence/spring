package com.bence.mate.spring.project.rest.resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.apache.commons.io.FileUtils;

import com.bence.mate.spring.project.rest.dto.Student;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.io.File;

@Slf4j
@RestController
@RequestMapping("/students")
public class StudentResource {

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public String retrieve() throws IOException {
		File input = ResourceUtils.getFile("classpath:input.json");
	    return FileUtils.readFileToString(input, StandardCharsets.UTF_8.name());
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Student> create(@RequestBody Student student) {
		log.info("{}", student);
	    return ResponseEntity.status(HttpStatus.CREATED).body(student);
	}
}
