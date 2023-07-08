package com.pisien.springbatch.repeat.skip;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *     <Skip Policy>
 *         - AlwaysSkipItemSkipPolicy      : 항상 skip 한다
 *         - ExceptionClassifierSkipPolicy : 예외대상을 분류하여 skip 여부를 결정한다
 *         - CompositeSkipPolicy           : 여러 SkipPolicy 를 탐색하면서 skip 여부를 결정한다
 *         - LimitCheckingItemSkipPolicy   : Skip 카운터 및 예외 등록 결과에 따라 skip 여부를 결정한다. 기본값으로 설정된다
 *         - NeverSkipItemSkipPolicy       : skip 을 하지 않는다
 *
 *         - 많이 복잡하니까..
 *         - 나중에 다시 공부 하자
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class SkipConfiguration {
    private final Logger logger = LoggerFactory.getLogger("SkipConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. skipJob ~!");
        return jobBuilderFactory.get("skipJob")
                .incrementer(new RunIdIncrementer())
                .start(skipStep())
                .build();
    }

    @Bean
    public Step skipStep() {
        logger.info("\t 2. skipStep Started~!");
        return stepBuilderFactory.get("skipStep")
                .<String, String>chunk(5)
                .reader(new ItemReader<String>() {
                    int i = 0;
                    @Override
                    public String read() {
                        i++;
                        if(i==20) {    // 10회까지 Skip 해라.
                            throw new IllegalArgumentException("Skiped~~~!");
                        }
                        logger.info("\t\t 2.1 ItemReader count = "+ i);
                        return i > 20 ? null : String.valueOf(i);
                    }
                })
                .processor(itemProcessor())
                .writer(itemWriter())
                .faultTolerant()
                .skip(SkippableException.class)                 // Skip exception 에러 적용
                .skipLimit(10)                                  // 최대 10번까지 Skip 허용
                //.skipPolicy(limitCheckingItemSkipPolicy())
                //.noSkip(NoSkippableException.class)           // Custom 절대 Skip 하지 마라 .
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())   // 항상 성공하게 만든다.
                //.skipPolicy(new NeverSkipItemSkipPolicy())    // 절대 Skip 하지 마라 .
                .build();
    }

    @Bean
    public SkipPolicy limitCheckingItemSkipPolicy() {
        Map<Class<? extends Throwable>, Boolean> exceptionClass = new HashMap<>();
        exceptionClass.put(SkippableException.class, true);
        LimitCheckingItemSkipPolicy limitCheckingItemSkipPolicy
                = new LimitCheckingItemSkipPolicy(4, exceptionClass);

        return limitCheckingItemSkipPolicy;
    }


    /**
     * ItemProcessor
     * */
    @Bean
    public ItemProcessor<? super String, String> itemProcessor() {
        return new SkipItemProcessor();
    }

    /**
     * ItemWriter
     * */
    @Bean
    public ItemWriter<? super String> itemWriter() {
        return new SkipItemWriter();
    }
}

