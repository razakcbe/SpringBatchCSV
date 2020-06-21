package com.batch.application.schedule;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarkdownSolrBatchScheduler {
	
	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;
    
    @Scheduled(fixedRate = 30000)
    public void schedule() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        /*jobLauncher.run(job, new JobParametersBuilder()
            .addDate("date", new Date())
            .toJobParameters());*/
    }

}
