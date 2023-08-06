package com.bence.mate.spring.project.rest.config;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.Job;

import com.bence.mate.spring.project.rest.reader.DatabseReader;
import com.bence.mate.spring.project.rest.reader.JsonReader;
import com.bence.mate.spring.project.rest.reader.RestReader;
import com.bence.mate.spring.project.rest.reader.CsvReader;
import com.bence.mate.spring.project.rest.reader.XmlReader;

import com.bence.mate.spring.project.rest.writer.DatabaseWriter;
import com.bence.mate.spring.project.rest.writer.SimpleWriter;
import com.bence.mate.spring.project.rest.writer.JsonWriter;
import com.bence.mate.spring.project.rest.writer.RestWriter;
import com.bence.mate.spring.project.rest.writer.CsvWriter;
import com.bence.mate.spring.project.rest.writer.XmlWriter;

import com.bence.mate.spring.project.rest.dto.Student;
import java.io.FileNotFoundException;

@Configuration(value = "Rest")
public class Batch {
	
	@Autowired
	private CsvReader csvReader;
	
	@Autowired
	private XmlReader xmlReader;
	
	@Autowired
	private JsonReader jsonReader;
	
	@Autowired
	private RestReader restReader;

	@Autowired
	private DatabseReader dabaseReader;
	
	// ---------------------------------
	
	@Autowired
	private CsvWriter csvWriter;
	
	@Autowired
	private XmlWriter xmlWriter;

	@Autowired
	private JsonWriter jsonWriter;
	
	@Autowired
	private RestWriter restWriter;

	@Autowired
	private SimpleWriter simpleWriter;
	
	@Autowired
	private DatabaseWriter databaseWriter;
	
    @Bean
    @Qualifier("thirdJob")
    protected Job thirdJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws FileNotFoundException {
        return new JobBuilder("Third Job", jobRepository)
        		.incrementer(new RunIdIncrementer())
        		.start(firstChunkStep(jobRepository, transactionManager))
        		.build();
    }
    
    protected Step firstChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws FileNotFoundException {
        return new StepBuilder("First chunk step", jobRepository)
        		.<Student, Student>chunk(3, transactionManager)
        		.reader(restReader.read())
        		.writer(restWriter.write())
        		.build();
    }
}
