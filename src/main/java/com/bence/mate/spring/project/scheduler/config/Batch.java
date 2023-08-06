package com.bence.mate.spring.project.scheduler.config;

import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.repeat.RepeatStatus;

import com.bence.mate.spring.example.listener.FirstStepListener;
import com.bence.mate.spring.example.listener.FirstJobListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

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
@Configuration(value = "Scheduled")
public class Batch {
	
	@Autowired
	private FirstJobListener firstJobListener;
	
	@Autowired
	private FirstStepListener firstStepListener;
	
    @Bean
    @Qualifier("fourthJob")
    protected Job fourthJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("Fourth Job", jobRepository)
				.incrementer(new RunIdIncrementer())
				.start(firstStep(jobRepository, transactionManager))
				.listener(firstJobListener)
				.build();
    }
    
    protected Step firstStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("First step", jobRepository)
				.tasklet(firstTask(), transactionManager)
				.listener(firstStepListener)
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
}
