package com.bence.mate.spring.project.rest.reader;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import com.bence.mate.spring.project.rest.dto.Student;

import java.io.FileNotFoundException;

@Component
public class CsvReader {

	public FlatFileItemReader<Student> read() throws FileNotFoundException {
        FlatFileItemReader<Student> flatFileItemReader = new FlatFileItemReader<>();

        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
        delimitedLineTokenizer.setNames("ID", "First Name", "Last Name", "Email");
        
        BeanWrapperFieldSetMapper<Student> studentFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        studentFieldSetMapper.setTargetType(Student.class);

        DefaultLineMapper<Student> studentLineMapper = new DefaultLineMapper<>();
        studentLineMapper.setLineTokenizer(delimitedLineTokenizer);
        studentLineMapper.setFieldSetMapper(studentFieldSetMapper);
        
        flatFileItemReader.setResource(new FileSystemResource(ResourceUtils.getFile("classpath:input.csv")));
        flatFileItemReader.setLineMapper(studentLineMapper);
        flatFileItemReader.setLinesToSkip(1);
        
        return flatFileItemReader;
	}
}
