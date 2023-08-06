package com.bence.mate.spring.project.rest.writer;

import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.io.IOException;
import java.io.Writer;

@Component
public class CsvWriter {

	public FlatFileItemWriter<Student> write() throws FileNotFoundException {
		BeanWrapperFieldExtractor<Student> beanWrapperFieldExtractor = new BeanWrapperFieldExtractor<>();
		beanWrapperFieldExtractor.setNames(new String[] {"id", "firstName", "lastName", "email"});
		
		DelimitedLineAggregator<Student> delimitedLineAggregator = new DelimitedLineAggregator<>();
		delimitedLineAggregator.setFieldExtractor(beanWrapperFieldExtractor);
		delimitedLineAggregator.setDelimiter(",");
		
		FlatFileItemWriter<Student> flatFileItemWriter = new FlatFileItemWriter<>();
		flatFileItemWriter.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:output.csv")));
		flatFileItemWriter.setHeaderCallback(new StudentFileHeaderCallback());
		flatFileItemWriter.setFooterCallback(new StudentFileFooterCallback());
		flatFileItemWriter.setLineAggregator(delimitedLineAggregator);
		
		return flatFileItemWriter;
	}
	
	private static class StudentFileHeaderCallback implements FlatFileHeaderCallback {

		@Override
		public void writeHeader(Writer writer) throws IOException {
			writer.write("Id,First Name,Last Name,Email");			
		}
	}
	
	private static class StudentFileFooterCallback implements FlatFileFooterCallback {

		@Override
		public void writeFooter(Writer writer) throws IOException {
			writer.write("Created @ " + LocalDate.now());
		}
	}
}
