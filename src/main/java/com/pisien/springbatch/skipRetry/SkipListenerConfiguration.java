package com.pisien.springbatch.skipRetry;

import com.pisien.springbatch.controller.RunIdIncrementer;
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
public class SkipListenerConfiguration {
    private final Logger logger = LoggerFactory.getLogger("SkipListenerConfiguration 의 로그");
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
                .processor(new ItemProcessor<Integer, String>() {
                    @Override
                    public String process(Integer item) throws Exception {
                        if(item.equals(4)) {
                            throw new CustomSkipException("process Skipped~~!");
                        }
                        return "item" + item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        for (String item : items) {
                            if(item.equals("item5")) {
                                throw new CustomSkipException("write Skipped~~!");
                            }
                            logger.info("write = " + item);
                        }
                    }
                })
                .faultTolerant()
                .skip(CustomSkipException.class)
                .skipLimit(4)
                .listener(new CustomSkipListener())
                .build();
    }

    @Bean
    public ItemReader<Integer> listItemReader() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        return new LinkedListItemReader<>(list);
    }
}
