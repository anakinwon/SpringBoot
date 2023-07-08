package com.pisien.springbatch.writer;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JdbcDBtoDBConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FlatFixedLenFilesConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   < DB를 읽어서 -> DB로 저장하기.>
     *       - 100 건씩 묶어서
     *       - 1,000건 처리해라.
     *       - select count(*) from Person;  -- 작업 전 건수 :   100 건
     *       - select count(*) from Person;  -- 작업 후 건수 : 1,100 건 정상 작동됨.
     *
     * */
    @Bean
    public Job job() {
        logger.info("1. jdbcBatchJob Start ");
        return this.jobBuilderFactory.get("jdbcBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(jdbcBatchStep())
                .build();
    }

    @Bean
    public Step jdbcBatchStep() {
        logger.info("\t2. jdbcBatchStep Start ");
        return stepBuilderFactory.get("jdbcBatchStep")
                .<Member, Member>chunk(1000)// chunk-size 설정 = Commit Interval
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Member> customItemReader() {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(1000);
        reader.setRowMapper(new MemberRowMapper());

        // select ~ from ~ where 세팅
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username < :username ");

        // 파라미터 세팅
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", 10001);
        reader.setParameterValues(parameters);

        // Order by 세팅
        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("password", Order.DESCENDING);
        queryProvider.setSortKeys(sortKeys);
        reader.setQueryProvider(queryProvider);

        return reader;
    }

    @Bean
    public ItemWriter<? super Member> customItemWriter() {
        logger.info("\t\t\t4. customItemWriter Start ");
        return new JdbcBatchItemWriterBuilder<Member>()
                .dataSource(dataSource)
                .sql(" Insert into person (password) values (:password) ")
                .beanMapped()
                .build();
    }

}
