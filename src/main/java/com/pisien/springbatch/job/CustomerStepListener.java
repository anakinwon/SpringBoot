package com.pisien.springbatch.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class CustomerStepListener implements JobExecutionListener {
    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext().putString("name2", "anakin2");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

    }
}
