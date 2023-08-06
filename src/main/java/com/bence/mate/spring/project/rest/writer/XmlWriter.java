package com.bence.mate.spring.project.rest.writer;

import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;

@Component
public class XmlWriter {

	public StaxEventItemWriter<Student> write() throws FileNotFoundException {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Student.class);

		StaxEventItemWriter<Student> staxEventItemWriter = new StaxEventItemWriter<Student>();
		staxEventItemWriter.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:output.xml")));
		staxEventItemWriter.setMarshaller(jaxb2Marshaller);
		staxEventItemWriter.setRootTagName("students");

		return staxEventItemWriter;
	}
}
