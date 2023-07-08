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
 *  FlatFiles Formatted Test
 *     - <파일 만들기 테스트>
 *     - D:\JpaNQueryDsl\springbatch\src\main\resources\
 *     - customerFomatted.txt
 *     - append 기능 확인
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FlatOBJtoFileFormattedConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFilesFormattedConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. flatFilesFormatted : 파일쓰기 시작.");
        return jobBuilderFactory.get("flatFilesDelimited")
                .incrementer(new RunIdIncrementer())
                .start(flatFilesFormattedStep())
                .build();
    }

    @Bean
    public Step flatFilesFormattedStep() throws Exception{
        logger.info("\t2. flatFilesFormatted-step1 : 스텝 시작.");
        return stepBuilderFactory.get("flatFilesFormatted")
                .<Customer, Customer>chunk(10)
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Customer> customItemReader() {

        logger.info("\t\t3. customItemReader : 파일을 작성할 내용 읽기");
        List<Customer> customers = Arrays.asList(
                new Customer(11L, "Anakin2" , 28),
                new Customer(12L, "Padme2"  , 32),
                new Customer(13L, "Asoka2"  , 18),
                new Customer(14L, "Obiwan2" , 52),
                new Customer(15L, "Yoda2"   , 232),
                new Customer(16L, "Duke2"   , 67),
                new Customer(17L, "Grivers2", 54)
        );
        ListItemReader<Customer> reader = new ListItemReader<>(customers);

        return reader;
    }

    @Bean
    public ItemWriter customItemWriter() {
        logger.info("\t\t\t4. customItemWriter : 파일을 쓰기");
        return new FlatFileItemWriterBuilder<Customer>()
                .name("flatFilesWriter")
                .resource(new FileSystemResource("D:\\JpaNQueryDsl\\springbatch\\src\\main\\resources\\customerFomatted.txt"))  // 파일 저장 위치
                .append(true)                               // 이미 파일이 있으면, 내용 추가해라!!~~
                .formatted()                                // 파일 구분 방식
                .format("%-3d%-10s%-3d")                    // 파일 자릿수
                .names(new String[]{"id","name","age"})     // 필드 제목
                .build();
    }


}
