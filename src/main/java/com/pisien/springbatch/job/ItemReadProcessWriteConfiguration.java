package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.reader.CustomItemProcessor;
import com.pisien.springbatch.reader.CustomItemReader;
import com.pisien.springbatch.reader.CustomItemWriter;
import com.pisien.springbatch.reader.Customer;
import lombok.RequiredArgsConstructor;
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
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class ItemReadProcessWriteConfiguration {
    private final Logger logger = LoggerFactory.getLogger("ItemReadProcessWriteConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <Flow Step 구성하기>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("statusJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    /**
     * <청크 기반 테스트하기>
     *     1. ListItemReader    : E-Extract
     *       > 2. ItemProcessor : T-Transform
     *          > 3. ItemWriter : L-Load
     * */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Customer, Customer>chunk(2)// chunk-size 설정 = Commit Interval
                .reader(itemReader())
                .processor(itemProcess())
                .writer(itemWriter())
                .build();
    }

    @Bean    // 총 6개 읽은 데이터 중 2개씩 청크로 묶어서
    public ItemReader<Customer> itemReader() {
        return new CustomItemReader(Arrays.asList(new Customer("anakin")
                , new Customer("Padme")
                , new Customer("Yoda")
                , new Customer("Obiwan")
                , new Customer("Griverse")
                , new Customer("Duke")
        )) ;
    }

    @Bean   // 읽은 2개를 대문자로 변환하고,
    public ItemProcessor<? super Customer, ? extends Customer> itemProcess() {
        return new CustomItemProcessor() ;
    }

    @Bean   // 대문자로 변환된 문자를 처리한다.
    public ItemWriter<? super Customer> itemWriter() {
        return new CustomItemWriter() ;
    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" step2 was Executed~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
