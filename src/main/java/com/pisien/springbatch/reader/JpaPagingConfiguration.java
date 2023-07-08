package com.pisien.springbatch.reader;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.jdbc.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JpaPagingConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JpaPagingConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory emf;

    /**
     *   <JPA 연결 읽기>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("jdbcCursorJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<Member, Member>chunk(10)
                .reader(jpaPagingItemReader())
                .writer(jpaPagingItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Member> jpaPagingItemReader() {

        /**
         * 파라미터 설정하기.
         * */
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", "101");

        /** 
         *   JPA -> JpaCursorItemReaderBuilder 확인
         * */
        return new JpaPagingItemReaderBuilder<Member>()
                .name("jpaPagingItemReader")
                .entityManagerFactory(emf)
//                .pageSize(10)
                .queryString(" select m from Member m where m.username < :username order by m.password desc ")   // JPQL 작성
                .parameterValues(parameters)            // 파라미터 전달.
                .maxItemCount(10)                       // 10건씩
                .build();
    }

    @Bean
    public ItemWriter<Member> jpaPagingItemWriter() {
        return items -> {
            for (Member item : items) {
                System.out.println("item = " + item.toString());
            }
        };
    }
}
