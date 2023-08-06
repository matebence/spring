package com.bence.mate.spring.project.rest.resource;

import com.bence.mate.spring.project.rest.dto.JobParamsRequest;
import com.bence.mate.spring.project.rest.service.JobService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.batch.core.launch.JobOperator;

import java.util.List;

@RestController
@RequestMapping("/job")
public class JobResource {
	
	@Autowired
	private JobService jobService;
	
	@Autowired
	private JobOperator jobOperator;

	@GetMapping("/start/{jobName}")
	public String startJob(@PathVariable String jobName, @RequestBody List<JobParamsRequest> jobParamsRequestList) {
		jobService.startJob(jobName, jobParamsRequestList);
		return "Job Started...";
	}
	
	// This ID can be found in the table BATCH_JOB_EXECUTION
	@GetMapping("/stop/{jobExecutionId}")
	public String stopJob(@PathVariable long jobExecutionId) {
		try {
			jobOperator.stop(jobExecutionId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Job Stopped...";
	}
}
