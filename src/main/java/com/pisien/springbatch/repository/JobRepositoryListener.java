package com.pisien.springbatch.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobRepositoryListener implements JobExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("JobRepositoryListener 의 로그");
    @Autowired JobRepository jobRepository;

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        String jobName = jobExecution.getJobInstance().getJobName();

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("requestDate", "20230625").toJobParameters();

        JobExecution lastJobExecution = jobRepository.getLastJobExecution(jobName, jobParameters);
        if (lastJobExecution != null) {
            for (StepExecution execution : lastJobExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                logger.info("status     = " + status);
                ExitStatus exitStatus = execution.getExitStatus();
                logger.info("exitStatus = " + exitStatus);
                String stepName = execution.getStepName();
                logger.info("stepName   = " + stepName);
            }

        }
    }

}
