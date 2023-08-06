package com.bence.mate.spring.project.scheduler.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.stereotype.Service;
import org.springframework.batch.core.Job;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobSchedulerService {
	
	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	@Qualifier("fourthJob")
	private Job fourthJob;

	// EVERY MINUTE
	@Scheduled(cron = "0 0/1 * 1/1 * ?")
	public void fourthJobStarter() {
        //with this we make sure that the jobs is always unique its the argument run=one in java
        JobParameters jobParameters = new JobParametersBuilder()
        		.addLong("currentTime", System.currentTimeMillis())
        		.toJobParameters();
        
        try {
        	JobExecution jobExecution = jobLauncher.run(fourthJob, jobParameters);
            log.info("Job Execution ID = {}", jobExecution.getId());
        }catch(Exception e) {
        	log.info("Exception while starting job");
        }
	}
}
