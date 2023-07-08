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
 *   <Simple Flow>
 *      - Transition 을 이용하여 중첩 Flow 를 만들어 수행할 수 있다.
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FlowNestedJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlowJobConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    // [JOB 선언 START] =============================================================
    @Bean
    public Job job() {
        return jobBuilderFactory.get("flowJob")
                .start(flow1())                        // flow1이 정상 수행되면,
                .on("COMPLETED").to(flow2()).on("FAILED").stop()    // flow2가 바로 수행된다.
                .from(flow1())                         // flow1이 오류가 발생하면,
                .on("FAILED").to(flow3())       // flow3가 바로 수행된다.
                .end()
                .build();
    }
    // [JOB 선언 END] ===============================================================


    // [Flow 선언 START] ============================================================
    @Bean
    public Flow flow1() {  // SimpleFlow1
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow1");
        flowBuilder.start(step1())
                .next(step2())
                .end();

        return flowBuilder.build();
    }
    @Bean
    public Flow flow2() {  // SimpleFlow2
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow2");
        flowBuilder.start(flow3())
                .next(step3())
                .next(step4())
                .end();

        return flowBuilder.build();
    }
    @Bean
    public Flow flow3() {  // SimpleFlow2
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow3");
        flowBuilder.start(step5())
                .next(step6())
                .end();

        return flowBuilder.build();
    }
    // [Flow 선언 END] ==============================================================


    // [Step 선언 START] ============================================================
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
                    logger.info(" \t\t\t\t\t step3 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t\t\t\t\t\t step4 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t\t\t step5 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    @Bean
    public Step step6() {
        return stepBuilderFactory.get("step6")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" \t\t\t\t step6 was Executed~~~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }
    // [Step 선언 END] ==============================================================

}
