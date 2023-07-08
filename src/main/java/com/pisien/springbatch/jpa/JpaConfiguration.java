package com.pisien.springbatch.jpa;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JpaConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JpaConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory entityManagerFactory;

    /**
     *   < DB를 읽어서 -> DB로 저장하기.>
     *
     * */
    @Bean
    public Job job() {
        logger.info("1. jpaBatchJob Start ");
        return this.jobBuilderFactory.get("jpaBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(jpaBatchStep())
                .build();
    }

    @Bean
    public Step jpaBatchStep() {
        logger.info("\t2. jpaBatchStep Start ");
        return stepBuilderFactory.get("jpaBatchStep")
                .<Member, Person>chunk(100)// chunk-size 설정 = Commit Interval
                .reader(customItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Member> customItemReader() {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(100);
        reader.setRowMapper(new MemberRowMapper());

        // select ~ from ~ where 세팅
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username < :username ");

        // 파라미터 세팅
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", 1001);
        reader.setParameterValues(parameters);

        // Order by 세팅
        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("password", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        return reader;
    }


    @Bean
    public ItemProcessor<? super Member, ? extends Person> customItemProcessor() {
        logger.info("\t\t\t4. jpaItemProcessor Start ");
        return new CustomJpaItemProcessor();
    }


    @Bean
    public ItemWriter<? super Person> customItemWriter() {
        logger.info("\t\t\t\t5. JpaItemWriterBuilder Start ");
        return new JpaItemWriterBuilder<Person>()
                .usePersist(true)
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

}
