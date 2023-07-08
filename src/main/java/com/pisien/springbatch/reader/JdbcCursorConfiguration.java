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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//@Configuration
@RequiredArgsConstructor
public class JdbcCursorConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JdbcCursorConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   <JDBC 연결 읽기>
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
                .reader(jdbcItemReader())
                .writer(jdbcItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Member> jdbcItemReader() {
        return new JdbcCursorItemReaderBuilder<Member>()
                .name("jdbcCursorItemReader")
                .fetchSize(10)
                .sql(" select username, password, created_dt from Member where username < ? order by password desc")
                .beanRowMapper(Member.class)
                .queryArguments("101")
                .dataSource(dataSource)
                .build();
    }


    @Bean
    public ItemWriter<Member> jdbcItemWriter() {
        return items -> {
            for (Member item : items) {
                System.out.println("item = " + item.toString());
            }
        };
    }

}
