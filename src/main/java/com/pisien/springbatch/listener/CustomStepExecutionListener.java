package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.stereotype.Component;


/**
 *  <인터페이스 방식 리스너>
 *      - Annotation 방식 Listener
 *      - implements StepExecutionListener
 *
 * */
@Component
public class CustomStepExecutionListener implements StepExecutionListener {
    private final Logger logger = LoggerFactory.getLogger("CustomJobExecutionListener 의 로그");

    @Override
    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        stepExecution.getExecutionContext().put("name", "step1");
        logger.info("\t\t 2. beforeStep is Started!");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {

        BatchStatus status = stepExecution.getStatus();
        logger.info("\t\t 2. afterStep - status = " + status);

        ExitStatus exitStatus = stepExecution.getExitStatus();
        logger.info("\t\t 2. afterStep - exitStatus = " +exitStatus);

        String stepName = (String)stepExecution.getExecutionContext().get("name");
        logger.info("\t\t 2. afterStep - stepName = " + stepName);

        return ExitStatus.COMPLETED;
    }


}
