package com.pisien.springbatch.writer;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.util.Arrays;
import java.util.List;

/**
 *  FlatFiles Delimited Test
 *     <파일 만들기 테스트>
 *     - D:\JpaNQueryDsl\springbatch\src\main\resources\
 *     - customerw.txt
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FlatOBJtoFileDelimitedConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFilesDelimitedConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. flatFilesDelimited : 파일쓰기 시작.");
        return jobBuilderFactory.get("flatFilesDelimited")
                .incrementer(new RunIdIncrementer())
                .start(flatFilesDelimitedStep())
                .build();
    }

    @Bean
    public Step flatFilesDelimitedStep() throws Exception{
        logger.info("\t2. flatFilesDelimited-step1 : 스텝 시작.");
        return stepBuilderFactory.get("flatFilesDelimitedStep")
                .<Customer, Customer>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> customItemReader() {

        logger.info("\t\t3. customItemReader : 파일을 작성할 내용 읽기");
        List<Customer> customers = Arrays.asList(
                new Customer(1L, "Anakin" , 28),
                new Customer(2L, "Padme"  , 32),
                new Customer(3L, "Asoka"  , 18),
                new Customer(4L, "Obiwan" , 52),
                new Customer(5L, "Yoda"   , 232),
                new Customer(6L, "Duke"   , 67),
                new Customer(7L, "Grivers", 54)
        );
        ListItemReader<Customer> reader = new ListItemReader<>(customers);

        return reader;
    }

    @Bean
    public ItemWriter<? super Customer> customItemWriter() {
        logger.info("\t\t\t4. customItemWriter : 파일을 쓰기");
        return new FlatFileItemWriterBuilder<Customer>()
                .name("flatFilesWriter")
                .resource(new FileSystemResource("D:\\JpaNQueryDsl\\springbatch\\src\\main\\resources\\customerw.txt"))  // 파일 저장 위치
                .delimited()                                // 파일 구분 방식
                .delimiter("|")                             // 파일 구분자
                .names(new String[]{"id","name","age"})     // 필드 제목
                .build();
    }


}
