package com.pisien.springbatch.job;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *   FlowJob
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FlowJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlowJobConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() {
        return jobBuilderFactory.get("flowJob")
                .start(flow())
                .next(step3())
                .end()
                .build();
    }

    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder.start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t step1 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t\t step2 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t\t\t step3 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }


//    /**
//     *    flowJob 테스트하기
//     *       - step1 작업이 성공하면, step2 수행하고
//     *       - step1 작업이 실패하면, step3 수행해라.
//     * */
//    @Bean
//    public Job flowJob() {
//        return this.jobBuilderFactory.get("flowJob")
//                .incrementer(new RunIdIncrementer())
//                .start(flowStep1())
//                .on("COMPLETED").to(flowStep2()) // flowStep1 이 성공하면, flowStep2 를 수행하고
//                .from(flowStep1())
//                .on("FAILED").to(flowStep3())    // flowStep1 이 실패하면, flowStep3 를 수행하고
//                .end()
//                .build();
//    }
//    @Bean
//    public Step flowStep1() {
//        return stepBuilderFactory.get("flowStep1")
//                .tasklet(new Tasklet() {  // 익명 클래스 사용
//                    @Override
//                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                        throw new RuntimeException("오류 발생");    // 오류 시 FAILED 가 아닌, "ABANDONED" 으로 저장됨
////                        return RepeatStatus.FINISHED;
//                    }
//                })
//                .build();
//    }

//    /***
//     *       - flowStep1 작업이 성공하면, flowStep2 수행하고
//     * */
//    @Bean
//    public Step flowStep2() {
//        return stepBuilderFactory.get("flowStep2")
//                .tasklet(new CustomTasklet2())
//                .build();
//    }
//
//    /***
//     *       - flowStep1 작업이 실패하면, flowStep3 수행해라.
//     * */
//    @Bean
//    public Step flowStep3() {
//        return stepBuilderFactory.get("flowStep3")
//                .tasklet(new CustomTasklet3())
//                .build();
//    }

}
