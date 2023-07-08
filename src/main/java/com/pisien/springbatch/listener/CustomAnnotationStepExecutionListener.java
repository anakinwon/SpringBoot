package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.batch.core.annotation.BeforeStep;

/**
 *  <애노테이션 방식 리스너>
 *      - Annotation 방식 Listener
 *      - @BeforeStep
 *      - @AfterStep
 *
 * */
public class CustomAnnotationStepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("CustomJobExecutionListener 의 로그");

    @BeforeStep
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        stepExecution.getExecutionContext().put("name", "step1");
        logger.info("Step is Started!");
    }

    @AfterStep
    public ExitStatus afterStep(StepExecution stepExecution) {

        BatchStatus status = stepExecution.getStatus();
        logger.info("status = " + status);

        ExitStatus exitStatus = stepExecution.getExitStatus();
        logger.info("exitStatus = " +exitStatus);

        String stepName = (String)stepExecution.getExecutionContext().get("name");
        logger.info("stepName = " + stepName);

        return ExitStatus.COMPLETED;
    }


}
