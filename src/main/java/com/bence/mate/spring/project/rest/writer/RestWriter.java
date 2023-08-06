package com.bence.mate.spring.project.rest.writer;

import com.bence.mate.spring.project.rest.service.StudentRestTemplate;
import com.bence.mate.spring.project.rest.dto.Student;

import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestWriter {
	
	@Autowired
	private StudentRestTemplate studentRestTemplate;
	
	public ItemWriterAdapter<Student> write() {
		ItemWriterAdapter<Student> itemWriterAdapter = new ItemWriterAdapter<>();
		itemWriterAdapter.setTargetObject(studentRestTemplate);
		itemWriterAdapter.setTargetMethod("create");
		
		return itemWriterAdapter;
	}
}
