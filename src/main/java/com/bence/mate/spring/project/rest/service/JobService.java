package com.bence.mate.spring.project.rest.service;

import com.bence.mate.spring.project.rest.dto.JobParamsRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Job;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
public class JobService {
	
	@Autowired
	@Qualifier("thirdJob")
	private Job thirdJob;

	@Autowired
    private JobLauncher jobLauncher;
    
    @Async
    public void startJob(String jobName, List<JobParamsRequest> jobParamsRequestList) {
        //with this we make sure that the jobs is always unique its the argument run=one in java
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addLong("currentTime", System.currentTimeMillis());
        
        jobParamsRequestList.stream().forEach(jobPramReq ->jobParametersBuilder.addString(jobPramReq.getParamKey(), jobPramReq.getParamValue()));
        JobParameters jobParameters = jobParametersBuilder.toJobParameters();
        
        try {
            JobExecution jobExecution = null;
            if(jobName.equals("Third Job")) {
                jobExecution = jobLauncher.run(thirdJob, jobParameters);
            }
            log.info("Job Execution ID = {}", jobExecution.getId());
        }catch(Exception e) {
        	log.info("Exception while starting job");
        }
    }
}
