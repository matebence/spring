package com.bence.mate.spring.example.config;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.repeat.RepeatStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import com.bence.mate.spring.example.listener.FirstStepListener;
import com.bence.mate.spring.example.listener.FirstJobListener;
import com.bence.mate.spring.example.service.SecondTask;

import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration(value = "Example")
public class Batch {

	/* DEPRECATED VERSION
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Bean
    public Job firstJob() {
        return jobBuilderFactory.get("First Job")
                .start(firstStep())
                .build();
    }

    private Step firstStep() {
        return stepBuilderFactory.get("First Step")
                .tasklet(firstTask())
                .build();
    }

    private Tasklet firstTask() {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is first tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }*/
	
	@Autowired
	private SecondTask secondTask;
	
	@Autowired
	private FirstItemReader firstItemReader;

	@Autowired
	private FirstItemWriter firstItemWriter;
	
	@Autowired
	private FirstJobListener firstJobListener;
	
	@Autowired
	private FirstStepListener firstStepListener;
	
	@Autowired
	private FirstItemProcessor firstItemProcessor;
	
    @Bean
    @Qualifier("firstJob")
    protected Job firstJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("First Job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(firstStep(jobRepository, transactionManager))
				.next(secondStep(jobRepository, transactionManager))
				.listener(firstJobListener)
				.build();
    }
    
    protected Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First step", jobRepository)
				.tasklet(firstTask(), transactionManager)
				.listener(firstStepListener)
				.build();
    }
    
    protected Step secondStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("Second step", jobRepository)
				.tasklet(secondTask, transactionManager)
				.build();
    }
    
    private Tasklet firstTask() {
        return new Tasklet() {

            @Override
            public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                log.info("This is first tasklet step");
                return RepeatStatus.FINISHED;
            }
        };
    }
    
    @Bean
    @Qualifier("secondJob")
    protected Job secondJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("Second Job", jobRepository)
        		.incrementer(new RunIdIncrementer())
        		.start(firstChunkStep(jobRepository, transactionManager))
        		.build();
    }
    
    protected Step firstChunkStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First chunk step", jobRepository)
        		.<Integer, Long>chunk(3, transactionManager)
        		.reader(firstItemReader)
        		.processor(firstItemProcessor)
        		.writer(firstItemWriter)
        		.build();
    }
}
