package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *   Incrementer
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class IncrementerConfiguration {
    private final Logger logger = LoggerFactory.getLogger("IncrementerConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job myIncreaseJob() {
        System.out.println("\t\t\t\n\n\nmyIncreaseJob.CustomJobParametersIncrementer\n\n\n");
        return this.jobBuilderFactory.get("myIncreaseJob")
                .incrementer(new RunIdIncrementer())     // API 호출 시 무용지물임.
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t step1 has Executed!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t\t step2 has Executed!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}
