package com.pisien.springbatch.listener;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.listener.CustomJobExecutionListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class JobStepListenerConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JobStepListenerConfiguration 의 로그");
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomStepExecutionListener customStepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .next(step3())
                .listener(new CustomJobExecutionListener())
                //.listener(new CustomAnnotationJobExecutionListener())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .listener(customStepBuilderFactory)
                .build();
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .listener(customStepBuilderFactory)
                .build();
    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet((contribution, chunkContext) -> RepeatStatus.FINISHED)
                .listener(customStepBuilderFactory)
                .build();
    }

}
