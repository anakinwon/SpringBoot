package com.pisien.springbatch.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;


/**
 *   프로세서가 두 개 이상일 때,,,
 *      - 체이닝으로 연결해서 처리하는 로직
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class CompositeItemConfiguration {
    private final Logger logger = LoggerFactory.getLogger("CompositeItemConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. compositeItemJob Started~!");
        return jobBuilderFactory.get("compositeItemJob")
                .incrementer(new RunIdIncrementer())
                .start(compositeItemStep())
                .build();
    }

    @Bean
    public Step compositeItemStep() {
        logger.info("\t 2. compositeItemStep Started~!");
        return stepBuilderFactory.get("compositeItemStep")
                .<String, String>chunk(10)
                .reader(new ItemReader<String>() {
                    // 테스트를 위한 익명클래스
                    int i = 0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        return i > 10 ? null : "item" + i ;
                    }
                })
                .processor(customItemProcessor())
                .writer(items -> logger.info("\t\t\t 3. items = " + items))
                .build();
    }

    @Bean
    public ItemProcessor<? super String, String> customItemProcessor() {
        List itemProcess = new ArrayList<>();
        itemProcess.add(new CustomItemProcessor1());
        itemProcess.add(new CustomItemProcessor2());
        itemProcess.add(new CustomItemProcessor3());

        return new CompositeItemProcessorBuilder<>()
                .delegates(itemProcess)
                .build();
    }


}
