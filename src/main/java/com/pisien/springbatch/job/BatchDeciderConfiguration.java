package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.decider.CustomDecider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class BatchDeciderConfiguration {
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
        return this.jobBuilderFactory.get("deciderJob")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .next(decider())
                .from(decider()).on("ODD").to(oddStep())
                .from(decider()).on("EVEN").to(evenStep())
                .end()
                .build();
    }

    @Bean
    public JobExecutionDecider decider() {
        return new CustomDecider();
    }

    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
//                        contribution.getStepExecution().setExitStatus(ExitStatus.FAILED);        // 작업코드(FAILED)를 강제로 실패시킨다.
                        logger.info("\t\t Main-step was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Bean
    public Step evenStep() {
        return stepBuilderFactory.get("evenStep")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t evenStep was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    public Step oddStep() {
        return stepBuilderFactory.get("oddStep")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info("\t\t oddStep was Executed!");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

}
