package com.pisien.springbatch.skipRetry;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.listener.CustomChunkListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 *  <skip Test>
 *      - Reader Skip    : 3번
 *      - Processor Skip : 4번
 *      - Writer Skip    : 5번
 *
 * */

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class RetryListenerConfiguration {
    private final Logger logger = LoggerFactory.getLogger("RetryListenerConfiguration 의 로그");
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Integer, String>chunk(10)
                .reader(listItemReader())
                .processor(new CustomItemProcessor())
                .writer(new CustomItemWriter())
                .faultTolerant()
                .skip(CustomSkipException.class)                    // skip 이 있어야 Retry 가 적용됨을 유의해야 함.
                .skipLimit(2)                                       // skip 이 있어야 Retry 가 적용됨을 유의해야 함.
                .retry(CustomRetryException.class)
                .retryLimit(2)
                .listener(new CustomRetryListener())
                .build();
    }

    @Bean
    public ItemReader<Integer> listItemReader() {
        List<Integer> list = Arrays.asList(1,2,3,4);
        return new LinkedListItemReader<>(list);
    }
}
