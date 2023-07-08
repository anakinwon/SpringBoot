package com.pisien.springbatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class StopWatchJobListener implements JobExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("FlatFixedLenFilesConfiguration 의 로그");

    @Override
    public void beforeJob(JobExecution jobExecution) {

    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long time = jobExecution.getEndTime().getTime() - jobExecution.getStartTime().getTime();
        logger.info(" \t===========================");
        logger.info(" \t\t 소요시간 = " + time);
        logger.info(" \t===========================");
    }
}
