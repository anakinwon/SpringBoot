package com.pisien.springbatch.repeat.retry;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *     <Retry Operations - #1/2>
 *         - AlwaysRetryPolicy              : 항상 재시도를 허용한다
 *         - ExceptionClassifierRetryPolicy : 예외대상을 분류하여 재시도 여부를 결정한다
 *         - CompositeRetryPolicy           : 여러 RetryPolicy 를 탐색하면서 재시도 여부를 결정한다
 *         - SimpleRetryPolicy              : 재시도 횟수 및 예외 등록 결과에 따라 재시도 여부를 결정한다. 기본값으로 설정된다
 *         - MaxAttemptsRetryPolicy         : MaxAttemptsRetryPolicy
 *         - TimeoutRetryPolicy             : 주어진 시간동안 재시도를 허용된다
 *         - NeverRetryPolicy               : 최초 한 번만 허용하고 그 이후로는 허용하지 않는다
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class RetryConfiguration {
    private final Logger logger = LoggerFactory.getLogger("RetryConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. retryJob ~!");
        return jobBuilderFactory.get("retryJob")
                .incrementer(new RunIdIncrementer())
                .start(retryStep())
                .build();
    }

    @Bean
    public Step retryStep() throws Exception {
        logger.info("\t 2. retryStep Started~!");
        return stepBuilderFactory.get("retryStep")
                .<String, String>chunk(3)
                .reader(readerItem())
                .processor(itemProcessor())
                .writer(items -> items.forEach(item -> logger.info(" \t\t\t3. ItemWriter = "+item)))
                .faultTolerant()
                .skip(RetryableException.class)                    // Skip 이 없으면, Retry 가 무한 반복됨.
                .skipLimit(3)                                      // Skip 이 없으면, Retry 가 무한 반복됨.
                //.retry(RetryableException.class)                 // Retry 적용
                //.retryLimit(2)                                   // 재시도 회수 세팅
                .retryPolicy(retryPolicy())                        // 재시도 정책을 Custom 으로 만들어 사용함.
                .build();
    }

    @Bean
    public RetryPolicy retryPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();

        exceptionClass.put(RetryableException.class, true);
        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy(2, exceptionClass);

        return simpleRetryPolicy;
    }

    @Bean
    public ListItemReader<String> readerItem() {
        List<String> items = new ArrayList<>();

        for (int i=0; i<10; i++) {
            items.add(String.valueOf(i));
        }
        return new ListItemReader<>(items);
    }

    /**
     * ItemProcessor
     * */
    @Bean
    public ItemProcessor<? super String, String> itemProcessor() {
        return new RetryItemProcessor();
    }

}

