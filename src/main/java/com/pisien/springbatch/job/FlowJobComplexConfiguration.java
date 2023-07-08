package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.tasklet.CustomTasklet2;
import com.pisien.springbatch.tasklet.CustomTasklet3;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *   FlowJob
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FlowJobComplexConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlowJobComplexConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /**
     *    flowJob 테스트하기
     *       - start > next 만 활용한 Flow 구성
     * */
    @Bean
    public Job flowJob() {
        return jobBuilderFactory.get("flowJob")
                .incrementer(new RunIdIncrementer())
                .start(flowA())
                .next(step3())
                .next(flowB())
                .next(step6())
                .end()
                .build();
    }

    @Bean
    public Flow flowA() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowA");
        flowBuilder.start(step1())
                .next(step2())
                .build();

        return flowBuilder.build();
    }

    @Bean
    public Flow flowB() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flowB");
        flowBuilder.start(step4())
                .next(step5())
                .build();

        return flowBuilder.build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("Step1")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t Step1 was Completed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("Step2")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t\t Step2 was Completed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step step3() {
        return stepBuilderFactory.get("Step3")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t\t\t Step3 was Completed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step step4() {
        return stepBuilderFactory.get("Step4")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t\t\t\t Step4 was Completed!");
                        throw new RuntimeException("Step4 error! during running Step4-Task....");
                        //return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step step5() {
        return stepBuilderFactory.get("Step5")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t\t\t\t\t Step5 was Completed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
    @Bean
    public Step step6() {
        return stepBuilderFactory.get("Step6")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" \t\t\t\t\t\t Step6 was Completed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

}
