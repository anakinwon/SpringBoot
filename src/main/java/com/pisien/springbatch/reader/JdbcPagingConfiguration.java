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
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class JdbcPagingConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JdbcPagingConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   <JDBC Paging 처리>
     *
     * */
    @Bean
    public Job job() throws Exception {
        return this.jobBuilderFactory.get("jdbcPagingJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Member, Member>chunk(10)
                .reader(jdbcPagingItemReader())
                .writer(jdbcPagingItemWriter())
                .build();
    }

    @Bean
    public ItemReader<Member> jdbcPagingItemReader() throws Exception {
        /**
         * 파라미터 설정하기.
         * */
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", "101");

        return new JdbcPagingItemReaderBuilder<Member>()
                .name("jdbcPagingItemReader")
//                .pageSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Member.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameters)
                .maxItemCount(10)                         // 10건씩
                .build();
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception{
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username < :username ");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("password", Order.DESCENDING);

        queryProvider.setSortKeys(sortKeys);

        return queryProvider.getObject();
    }

    @Bean
    public ItemWriter<Member> jdbcPagingItemWriter() {
        return items -> {
            for (Member item : items) {
                logger.info("item = " + item.toString());
            }
        };
    }

}
