package com.pisien.springbatch.operation;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 *   Job Registry 등록하기
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class JobOperationConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFixedLenFilesConfiguration 의 로그");

    // 의존성 주입받기.
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final JobRegistry jobRegistry;

    @Bean
    public Job job() throws Exception {

        return jobBuilderFactory.get("bathJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    logger.info("Step1 was Executed~~!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    @Bean
    public Step step2() throws Exception {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info("Step2 was Executed~~!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }


    @Bean
    public BeanPostProcessor jobRegistryBeanPostProcessor() {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);

        return jobRegistryBeanPostProcessor;

    }


}
