package com.bence.mate.spring.project.rest.reader;

import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;

@Component
public class XmlReader {

	public StaxEventItemReader<Student> read() throws FileNotFoundException {
		StaxEventItemReader<Student> staxEventItemReader = new StaxEventItemReader<Student>();
		
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Student.class);

		staxEventItemReader.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:input.xml")));
		staxEventItemReader.setFragmentRootElementName("student");
		staxEventItemReader.setUnmarshaller(jaxb2Marshaller);

		return staxEventItemReader;
	}
}
