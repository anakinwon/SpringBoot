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
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JpaCursorConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JdbcCursorConfiguration 의 로그");

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
                .reader(jpaItemReader())
                .writer(jpaItemWriter())
                .build();
    }

    @Bean
    public ItemReader<? extends Member> jpaItemReader() {

        /**
         * 파라미터 설정하기.
         * */
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", "101");

        /** 
         *   JPA -> JpaCursorItemReaderBuilder 확인
         * */
        return new JpaCursorItemReaderBuilder<Member>()
                .name("jpaCursorItemReader")
                .entityManagerFactory(emf)
                .queryString(" select m from Member m where m.username < :username ")   // JPQL 작성
                .parameterValues(parameters)    // 파라미터 전달.
                .build();
    }

    @Bean
    public ItemWriter<Member> jpaItemWriter() {
        return items -> {
            for (Member item : items) {
                System.out.println("item = " + item.toString());
            }
        };
    }
}
