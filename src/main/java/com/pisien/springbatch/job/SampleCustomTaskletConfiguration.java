package com.pisien.springbatch.job;

import com.pisien.springbatch.tasklet.CustomTasklet1;
import com.pisien.springbatch.tasklet.CustomTasklet2;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//@Configuration
@RequiredArgsConstructor
public class SampleCustomTaskletConfiguration {

    private final Logger logger = LoggerFactory.getLogger("SampleCustomTaskletConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job sampleJob() {
        return jobBuilderFactory.get("sampleJob")
                .start(step1())                                // 최초 실행되는 스탭 정의
                .next(step2())                                 // 다음 실행되는 스탭 정의
                .build();                                      // Simple Job 생성.
    }

    @Bean
    public Step step1() {
      return stepBuilderFactory.get("step1")
              .tasklet((contribution, chunkContext) -> {
                  System.out.println(" \t Hello Spring Batch Step1 ");
                  return RepeatStatus.FINISHED;
              }).build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t\t Hello Spring Batch Step2 ");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
