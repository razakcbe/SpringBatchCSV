package com.batch.application.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.batch.application.model.JobExecutionStatus;

@RestController
@RequestMapping("/upload")
public class UploadController {

	@Autowired
	JobLauncher jobLauncher;
	
	@Autowired
	JobExplorer jobExplorer;

	@Autowired
	Job job;

	@GetMapping
	public String load() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException,
			JobInstanceAlreadyCompleteException {

		Map<String, JobParameter> maps = new HashMap<>();
		maps.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters parameters = new JobParameters(maps);
		JobExecution jobExecution = jobLauncher.run(job, parameters);

		System.out.println("JobExecution: " + jobExecution.getStatus());

		System.out.println("Batch is Running...");
		while (jobExecution.isRunning()) {
			System.out.println("...");
		}

		return "JobExecution Id " + jobExecution.getJobId();
	}
	
	@GetMapping("/job/{id}")
	public JobExecutionStatus getJobStatus(@PathVariable("id") Long executionId) {
		JobExecution execution = jobExplorer.getJobExecution(executionId);
		JobExecutionStatus result = new JobExecutionStatus(execution.getId(), execution.getStartTime(), execution.getCreateTime(), 
				execution.getEndTime(), execution.getLastUpdated(), execution.getExitStatus(), execution.getStatus());
		return result;
	}
}
