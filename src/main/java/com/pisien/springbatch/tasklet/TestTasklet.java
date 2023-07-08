package com.pisien.springbatch.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class TestTasklet implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger("TestTasklet 의 로그");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws  Exception {

        logger.info("\t Step1. 테스트 테스크릿 사용!!!");
        // 3초 딜레이.
        Thread.sleep(3000);

        String stepName = contribution.getStepExecution().getStepName();
        String jobName = chunkContext.getStepContext().getJobName();

        // Contribution 파라미터 참조 방법 : 객체를 반환한다. (추천방식)
        JobParameters param = contribution.getStepExecution().getJobExecution().getJobParameters();
        // Chunk 파라미터 참조 방법  : Map 으로 값만 확인할 수 있다.

        logger.info("\t1. stepName  = " + stepName);
        logger.info("\t1. jobName   = " + jobName);
        logger.info("\t1. run.id    = " + param.getString("run.id"));

        return RepeatStatus.FINISHED;
    }
}
