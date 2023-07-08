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
public class CustomTasklet1 implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger("CustomTasklet1 의 로그");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws  Exception {

        logger.info("\t Step1. 커스텀 테스크릿 사용!!!");

//        logger.info(" \t  Tasklet1. getReadCount        = "+contribution.getReadCount()       );
//        logger.info(" \t  Tasklet1. getWriteCount       = "+contribution.getWriteCount()      );
//        logger.info(" \t  Tasklet1. getFilterCount      = "+contribution.getFilterCount()     );
//        logger.info(" \t  Tasklet1. getSkipCount        = "+contribution.getSkipCount()       );
//        logger.info(" \t  Tasklet1. getReadSkipCount    = "+contribution.getReadSkipCount()   );
//        logger.info(" \t  Tasklet1. getWriteSkipCount   = "+contribution.getWriteSkipCount()  );
//        logger.info(" \t  Tasklet1. getProcessSkipCount = "+contribution.getProcessSkipCount());
//        logger.info(" \t  Tasklet1. getStepSkipCount    = "+contribution.getStepSkipCount()   );

          // Contribution 파라미터 참조 방법 : 객체를 반환한다. (추천방식)
          JobParameters param = contribution.getStepExecution().getJobExecution().getJobParameters();
          // Chunk 파라미터 참조 방법  : Map 으로 값만 확인할 수 있다.
          //Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
//          logger.info("\t1.  str = " + param.getString("name")    );
//          logger.info("\t1. lnum = " + param.getLong("seq")       );
//          logger.info("\t1.   dt = " + param.getString("createDt"));
//        //logger.info("\t1.   dt = " + param.getDate("createDt"));
//          logger.info("\t1. dnum = " + param.getDouble("pi")      );

        logger.info("\t1. name   = " + param.getString("name"));
        logger.info("\t1. run.id = " + param.getString("run.id"));
//        logger.info("\t1. date   = " + param.getDate("date"));

          Thread.sleep(2000);

        return RepeatStatus.FINISHED;
    }
}
