package com.bence.mate.spring.project.rest.service;

import com.bence.mate.spring.project.rest.dto.Student;

import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class StudentRestTemplate {

	private List<Student> result;

	public Student retrieve(long id, String name) {
		log.info("id = {} and name = {}", id, name);

		if (result == null) {
			RestTemplate restTemplate = new RestTemplate();
			Student[] students = restTemplate.getForObject("http://localhost:8080/students", Student[].class);
			result = new ArrayList<>(Arrays.asList(students));
		}

		return !result.isEmpty() ? result.remove(0) : null;
	}
	
	public Student create(Student student) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<Student> request = new HttpEntity<>(student, headers);
		
		return new RestTemplate().postForObject("http://localhost:8080/students", request, Student.class);
	}
}
