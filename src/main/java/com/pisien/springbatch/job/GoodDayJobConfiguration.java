package com.pisien.springbatch.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

//@Configuration
public class GoodDayJobConfiguration {
    private final Logger logger = LoggerFactory.getLogger("GoodDayJobConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public GoodDayJobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public Job goodDayJop() {

        return jobBuilderFactory.get("goodDayJop")
                .start(goodDayStep1())
                .next(goodDayStep2())
                .build();
    }

    @Bean
    public Step goodDayStep1() {
        return stepBuilderFactory.get("goodDayStep1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t Hello Spring Batch goodDayStep1 ");
                    return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step goodDayStep2() {
        return stepBuilderFactory.get("goodDayStep2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t\t Hello Spring Batch goodDayStep2 ");
                    return RepeatStatus.FINISHED;
                }).build();
    }


}
