package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 *  <인터페이스 방식 리스너>
 *      - Annotation 방식 Listener
 *      - implements JobExecutionListener
 *
 * */
public class CustomJobExecutionListener implements JobExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("CustomJobExecutionListener 의 로그");

    @Override
    public void beforeJob(JobExecution jobExecution) {
        logger.info("\t 1. beforeJob is Started!");
        logger.info("\t 1. beforeJob name : " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        long startTime = jobExecution.getStartTime().getTime();
        long endTime   = jobExecution.getEndTime().getTime();

        logger.info("\t1. afterJob 시작 시간  : " + startTime);
        logger.info("\t1. afterJob 종료 시간  : " + endTime);
        logger.info("\t1. afterJob 총소요 시간 : " + ( endTime - startTime));
    }
}
