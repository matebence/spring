package com.bence.mate.spring.example.listener;

import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobExecution;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FirstJobListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
        log.info("Before Job " + jobExecution.getJobInstance().getJobName());
        log.info("Job Params " + jobExecution.getJobParameters());
        log.info("Job Exec Context " + jobExecution.getExecutionContext());
        
        jobExecution.getExecutionContext().put("jec", "jec value");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
        log.info("After Job {}", jobExecution.getJobInstance().getJobName());
        log.info("Job Params {}", jobExecution.getJobParameters());
        log.info("Job Exec Context {}", jobExecution.getExecutionContext());	
	}
}
