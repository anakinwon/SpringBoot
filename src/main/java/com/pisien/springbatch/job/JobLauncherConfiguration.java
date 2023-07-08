package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.tasklet.CustomTasklet1;
import com.pisien.springbatch.tasklet.CustomTasklet2;
import com.pisien.springbatch.tasklet.CustomTasklet3;
import com.pisien.springbatch.tasklet.CustomTasklet5;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *   <작업 상태 코드>
 *   1. BatchStatus.STARTING, STARTED, STOPPING, STOPPED
 *                 , UNKOWN, ABANDONED, FAILED, COMPLETED.
 *   2. ExitStatus.EXECUTING, UNKOWN, NOOP
 *                , STOPPED, FAILED, COMPLETED.
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class JobLauncherConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JobLauncherConfiguration 의 로그");

    @Autowired private final JobBuilderFactory jobBuilderFactory;
    @Autowired private final StepBuilderFactory stepBuilderFactory;
    @Autowired private final JobExecutionListener jobRepositoryListener;

    @Bean
    public Job incrementerJob() {
        return this.jobBuilderFactory.get("incrementerJob")
                /* step start */
                .start(step1())
                .next(step2())
                // 기존 구현체
                .incrementer(new RunIdIncrementer())
                // 커스텀 가능
                // 항상 변하는 값 이 생겼다. JOB 을 성공해도 해당 JOB은 계속 실행이 가능하다.
                // 계속 변하는 값 + 동일한 파라미터 -> JobParameters 가 되므로 다른걸로 인식된다.
//                .incrementer(new CustomIncrementer())
                .build();
    }


//    /**
//     *  Flow Job 예제
//     * */
//    @Bean
//    public Job run() {
//        return this.jobBuilderFactory.get("flowBatch")
//                .start(flow())
//                .next(step4())
////                .next(step5())
////                .next(step6())
//                .end()
//                .incrementer(new CustomJobParametersIncrementer())
//                .build();
//    }
//
//    @Bean
//    public Flow flow() {
//        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
//        flowBuilder.start(step1())
//                .next(step2())
////                .next(step3())
//                .end();
//
//        return flowBuilder.build();
//    }
    /**
     *  Flow Job 예제
     * */



    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new CustomTasklet1() {
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new CustomTasklet2() {
                })
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new CustomTasklet3() {
                })
                .build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
//                .tasklet(new CustomTasklet4() {
//                })
//                .build();
                .tasklet((contribution, chunkContext) -> {
                    // SKIP 하기 테스트
                    chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.ABANDONED);

                    contribution.setExitStatus(ExitStatus.STOPPED);
                    System.out.println(" \t\t step4 has ABANDONED!!");
                    return null;
                })
                .build();
    }
    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet(new CustomTasklet5() {
                })
                .build();
    }
    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    // 에러 테스트
                    chunkContext.getStepContext().getStepExecution().setStatus(BatchStatus.FAILED);

                    contribution.setExitStatus(ExitStatus.STOPPED);
                    System.out.println(" \t\t step3 has Executed!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
