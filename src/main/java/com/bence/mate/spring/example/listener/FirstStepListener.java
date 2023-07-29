package com.bence.mate.spring.example.listener;

import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FirstStepListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
        log.info("Before Step " + stepExecution.getStepName());
        log.info("Step Exec Cont " + stepExecution.getExecutionContext());
        log.info("Job Exec Cont " + stepExecution.getJobExecution().getExecutionContext());
        
        stepExecution.getExecutionContext().put("sec", "sec value");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("After Step " + stepExecution.getStepName());
        log.info("Step Exec Cont " + stepExecution.getExecutionContext());
        log.info("Job Exec Cont " + stepExecution.getJobExecution().getExecutionContext());
        return null;
	}	
}
