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
public class CustomTasklet6 implements Tasklet {
    private final Logger logger = LoggerFactory.getLogger("SampleCustomTaskletConfiguration의 로그");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws  Exception {
        logger.info("\t Step6. 커스텀 테스크릿 사용!!!");

//        logger.info(" \t  Tasklet6. getReadCount        = "+contribution.getReadCount()       );
//        logger.info(" \t  Tasklet6. getWriteCount       = "+contribution.getWriteCount()      );
//        logger.info(" \t  Tasklet6. getFilterCount      = "+contribution.getFilterCount()     );
//        logger.info(" \t  Tasklet6. getSkipCount        = "+contribution.getSkipCount()       );
//        logger.info(" \t  Tasklet6. getReadSkipCount    = "+contribution.getReadSkipCount()   );
//        logger.info(" \t  Tasklet6. getWriteSkipCount   = "+contribution.getWriteSkipCount()  );
//        logger.info(" \t  Tasklet6. getProcessSkipCount = "+contribution.getProcessSkipCount());
//        logger.info(" \t  Tasklet6. getStepSkipCount    = "+contribution.getStepSkipCount()   );


        // Contribution 파라미터 참조 방법 : 객체를 반환한다. (추천방식)
        JobParameters param = contribution.getStepExecution().getJobExecution().getJobParameters();
        // Chunk 파라미터 참조 방법  : Map 으로 값만 확인할 수 있다.
        //Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
//        logger.info("\t\t\t\t6.  str = " + param.getString("name")    );
//        logger.info("\t\t\t\t6. lnum = " + param.getLong("seq")       );
//        logger.info("\t\t\t\t6.   dt = " + param.getString("createDt"));
//        logger.info("\t\t\t\t6. dnum = " + param.getDouble("pi")      );

        logger.info("\t\t\t\t\t\t6.   id   = " + param.getString("id"));
//        logger.info("\t\t\t\t\t\t6.   date = " + param.getDate("date"));
//        throw new RuntimeException("Step6. 커스텀 테스크릿 실행 시 오류가 발생했습니다.!!! ");

        Thread.sleep(2000);

        return RepeatStatus.FINISHED;
    }
}
