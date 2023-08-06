package com.bence.mate.spring.project.rest.reader;

import com.bence.mate.spring.project.rest.service.StudentRestTemplate;
import com.bence.mate.spring.project.rest.dto.Student;

import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestReader {

	@Autowired
	private StudentRestTemplate studentRestTemplate;
	
	public ItemReaderAdapter<Student> read() {
		ItemReaderAdapter<Student> itemReaderAdapter = new ItemReaderAdapter<>();
		
		itemReaderAdapter.setTargetObject(studentRestTemplate);
		itemReaderAdapter.setTargetMethod("retrieve");
		itemReaderAdapter.setArguments(new Object[] {1L, "Test"});
		
		return itemReaderAdapter;
	}
}
