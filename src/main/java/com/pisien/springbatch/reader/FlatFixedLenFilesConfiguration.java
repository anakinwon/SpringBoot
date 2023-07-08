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
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class FlatFixedLenFilesConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFixedLenFilesConfiguration 의 로그");

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
                .<String, String>chunk(2)// chunk-size 설정 = Commit Interval
                .reader(itemReader())
//                .processor(itemProcess())
                .writer( new ItemWriter() {
                    @Override
                    public void write(List items) throws Exception {
                        for (Object item : items) {
                            logger.info("item = " + item);
                        }
                        Thread.sleep(2000);
                    }
                })
                .build();
    }

    /**
     *  스프링배치에서 제공하는 API 이용 방식
     *     - BeanWrapperFieldSetMapper
     * */
    @Bean
    public ItemReader itemReader() {
        return new FlatFileItemReaderBuilder<Customer>()
                .name("flateFile")
                .resource(new ClassPathResource("/customer.txt"))   // 읽어올 파일
                //.fieldSetMapper(new CustomerFieldSetMapper())     // 직접 만들어서 매핑하는 파일
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>())  // 이 내장함수를 이용하면, 파일 만들 필요 없다.
                .targetType(Customer.class)                         // 그 대신 요거 추가...
                .linesToSkip(1)                                     // 첫째 줄 제목 제외
                .fixedLength()                                      // 구분자
//                .strict(true)                                       // 길이 체크를 엄격하게 함. Parsing error at line: 2
                .addColumns(new Range(1,7))                         // 범위 Range(1) 마지막은 생략하면 끝까지 읽어옴
                .addColumns(new Range(8,11))                        // 범위 Range(8) 마지막은 생략하면 끝까지 읽어옴
                .addColumns(new Range(12,14))                       // 범위 Range(12) 마지막은 생략하면 끝까지 읽어옴
                .names("name","year","age")                         // 필드 제목
                .build();
    }

    /**
     *  직접 코딩 방식
     * */
//    @Bean
//    public ItemReader itemReader() {
//        FlatFileItemReader<com.pisien.springbatch.flatdelimitedfiles.Customer> itemReader = new FlatFileItemReader<>();
//        itemReader.setResource(new ClassPathResource("/customer.csv"));
//
//        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();
//        lineMapper.setLineTokenizer(new DelimitedLineTokenizer());
//        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
//
//        itemReader.setLineMapper(lineMapper);
//        itemReader.setLinesToSkip(1);
//
//        return itemReader;
//    }

    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" step2 was Executed~!");
                    return RepeatStatus.FINISHED;
                }).build();
    }

}
