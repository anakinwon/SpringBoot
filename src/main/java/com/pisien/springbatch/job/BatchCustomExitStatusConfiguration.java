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
public class BatchCustomExitStatusConfiguration {
    private final Logger logger = LoggerFactory.getLogger("BatchStatusExitStatusConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;


    /**  <배치작업 상태코드>
     *   1. BatchStatus  : COMPLETED, STARTING, STARTED, STOPPING, STOPPED, FAILED, ABANDONED, UNKNOWN
     *   2. ExitStatus   : UNKNOWN, EXECUTING, COMPLETED, NOOP, FAILED, STOPPED
     *   3. FlowExecutionStatus : COMPLETED, STOPPED, FAILED, UNKNOWN

     *   <사용자 정의 상태코드>
     *   4. CustomStatus : COMPLETED, STOPPED, FAILED, UNKNOWN
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("statusJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .on("FAILED")   // step1 이 실패하면, step2 를 실행해라!
                .to(step2())
                .on("ANAKIN_CODE")
                .stop()
                .end()
                .build();
    }
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);        // 작업코드(FAILED)를 강제로 실패시킨다.
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
                        return RepeatStatus.FINISHED;
                    }
                })
                .listener(new PassCheckingListener())
                .build();
    }

}
