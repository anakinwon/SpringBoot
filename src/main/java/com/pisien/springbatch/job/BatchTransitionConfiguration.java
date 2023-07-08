package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class BatchTransitionConfiguration {
    private final Logger logger = LoggerFactory.getLogger("BatchTransitionConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /**
     *   <배치작업 상태코드>
     *   1. BatchStatus : COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN
     *   2. ExitStatus  : UNKNOWN, EXECUTING, COMPLETED, NOOP, FAILED, STOPPED
     *   3. FlowExecutionStatus : COMPLETED, STOPPED, FAILED, UNKNOWN
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("statusJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                    .on("A")
                    .to(step2())
                    .on("*")
                    .stop()
                .from(step1())
                    .on("*")
                    .to(step3())
                    .next(step4())
                .from(step2())
                    .on("*")
                    .to(step5())
                    .end()                 // COMPLETED
                .build();
    }
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t step1 was Executed!");
                        contribution.setExitStatus(ExitStatus.NOOP);        // 작업코드(FAILED)를 강제로 실패시킨다.
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t step2 was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t\t step3 was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step step4() {
        return stepBuilderFactory.get("step4")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t\t\t step4 was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }


    @Bean
    public Step step5() {
        return stepBuilderFactory.get("step5")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t\t\t\t step5 was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }
}
