package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class ParamLazyBindingConfiguration {
    private final Logger logger = LoggerFactory.getLogger("BatchStatusExitStatusConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <Flow Step 구성하기>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("statusJob")
                .incrementer(new RunIdIncrementer())
                .start(step1(null))
                .next(step2())
                .listener(new CustomJobListener())
                .build();
    }
    @Bean
    @JobScope
    public Step step1(@Value("#{jobParameters['message']}")  String message) {
        logger.info(" message has Executed!! " + message);
        return stepBuilderFactory.get("step1")
                .tasklet(tasklet1(null))  //익명 클래스 사용
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet(tasklet2(null))  //익명 클래스 사용
                .listener(new CustomerStepListener())
                .build();
    }

    @Bean
    @StepScope
    public Tasklet tasklet1(@Value("#{JobExecutionContext['name']}") String name) {
        logger.info("name = " + name);
        return (stepContribution, chunkContext) -> {
            logger.info(" tasklet1 has Executed!! ");
            return RepeatStatus.FINISHED;
        };
    }
    @Bean
    @StepScope
    public Tasklet tasklet2(@Value("#{JobExecutionContext['name2']}") String name2) {
        logger.info("name2 = " + name2);
        return (stepContribution, chunkContext) -> {
            logger.info(" tasklet2 has Executed!! ");
            return RepeatStatus.FINISHED;
        };
    }


}
