package com.pisien.springbatch.async;

import com.pisien.springbatch.config.StopWatchJobListener;
import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class AsyncDBtoDBConfiguration {
    private final Logger logger = LoggerFactory.getLogger("AsyncDBtoDBConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   < DB를 읽어서 -> DB로 저장하기.>
     *
     * */
    @Bean
    public Job job() throws Exception{
        logger.info("1. asyncBatchJob Start ");
        return this.jobBuilderFactory.get("asyncBatchJob")
                .incrementer(new RunIdIncrementer())
                //.start(asyncStep())
                .start(asyncStep1())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step asyncStep() throws Exception{
        logger.info("\t2. asyncStep Start ");
        return stepBuilderFactory.get("asyncStep")
                .<Member, Member>chunk(10)
                .reader(pagingItemReader())
                .processor(customItemProcessor())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public Step asyncStep1() throws Exception{
        logger.info("\t2. asyncStep1 Start ");
        return stepBuilderFactory.get("asyncStep1")
                .<Member, Member>chunk(10)
                .reader(pagingItemReader())
                .processor(asyncItemProcessor())
                .writer(asyncItemWriter())
                .build();
    }
    @Bean
    public AsyncItemWriter asyncItemWriter() {
        AsyncItemWriter<Member> asyncItemWriter = new AsyncItemWriter<>();
        asyncItemWriter.setDelegate((ItemWriter<Member>) customItemWriter());
        return asyncItemWriter;
    }

    @Bean
    public AsyncItemProcessor asyncItemProcessor() throws InterruptedException{
        AsyncItemProcessor<Member, Member> asyncItemProcessor = new AsyncItemProcessor<>();
        asyncItemProcessor.setDelegate(customItemProcessor());
        asyncItemProcessor.setTaskExecutor(new SimpleAsyncTaskExecutor());

        return asyncItemProcessor;
    }

    @Bean
    public ItemProcessor<Member, Member> customItemProcessor() throws InterruptedException {
        return new ItemProcessor<Member, Member>() {
            @Override
            public Member process(Member item) throws Exception {
                Thread.sleep(300);
                return new Member( item.getUsername()
                                 , item.getPassword().toUpperCase()
                                 , item.getCreatedDt());
            }
        };
    }


    @Bean
    public JdbcPagingItemReader<Member> pagingItemReader() {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
        reader.setRowMapper(new MemberRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username < :username ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", 101);
        reader.setParameterValues(parameters);

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
