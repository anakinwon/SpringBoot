package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;

/**
 *  <애노테이션 방식 리스너>
 *      - Annotation 방식 Listener
 *      - @BeforeJob
 *      - @AfterJob
 *
 * */
public class CustomAnnotationJobExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("CustomJobExecutionListener 의 로그");

    @BeforeJob
    public void beforeJob(JobExecution jobExecution) {
        logger.info("Job is Started!");
        logger.info("Job name : " + jobExecution.getJobInstance().getJobName());
    }

    @AfterJob
    public void afterJob(JobExecution jobExecution) {
        long startTime = jobExecution.getStartTime().getTime();
        long endTime   = jobExecution.getEndTime().getTime();

        logger.info("Job 시작 시간  : " + startTime);
        logger.info("Job 종료 시간  : " + endTime);
        logger.info("Job 총소요 시간 : " + ( endTime - startTime));
    }
}
