package com.pisien.springbatch.parallel;

import com.fasterxml.jackson.core.ObjectCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomTasklet implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger("CustomTasklet 의 로그");
//    private long sum;
    // 동시성 보장을 위한 Lock 설정
    private Object lock = new Object();

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws  Exception {

        logger.info("\t Step1. 커스텀 테스크릿 사용!!!");

        long sum=0;
        // 동시성 보장을 위한 락 처리 메커니즘...
//        synchronized (lock) {
            for(int i=0; i<1000; i++) {
                sum++;
            }

            logger.info(String.format("%s has been executed on thread %s"
                            , chunkContext.getStepContext()
                            , Thread.currentThread().getName()
                    )
            );
            logger.info("sum = " + sum);
//        }

        return RepeatStatus.FINISHED;
    }
}
