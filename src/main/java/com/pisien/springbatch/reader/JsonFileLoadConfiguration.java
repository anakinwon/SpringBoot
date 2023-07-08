package com.pisien.springbatch.reader;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.flatdelimitedfiles.Customer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
@RequiredArgsConstructor
public class JsonFileLoadConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JsonFileLoadConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <JSON 파일 읽고 쓰기>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("jsonFileLoad")
                .incrementer(new RunIdIncrementer())
                .start(step1())
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
                .<Customer, Customer>chunk(3)
                .reader(jsonItemReader())
                .writer(jsonItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> jsonItemReader() {
        return new JsonItemReaderBuilder<Customer>()
                .name("jsonReader")
                .resource(new ClassPathResource("customer.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(Customer.class))
                .build();
    }

    /**
     *  스프링배치에서 제공하는 API 이용 방식
     *     - BeanWrapperFieldSetMapper
     * */
    @Bean
    public ItemWriter<Customer> jsonItemWriter() {
        return items -> {
            for (Customer item : items) {
                System.out.println("item = " + item.toString());
            }
        };
    }

}
