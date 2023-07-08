package com.pisien.springbatch.thread;

import com.pisien.springbatch.config.StopWatchJobListener;
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
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class MultiThreadConfiguration {
    private final Logger logger = LoggerFactory.getLogger("MultiThreadConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   <Multi Thread DB 저장하기.>
     *
     * */
    @Bean
    public Job job() throws Exception {
        logger.info("1. jdbcBatchJob Start ");
        return this.jobBuilderFactory.get("threadBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(threadStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step threadStep() throws Exception {
        logger.info("\t2. threadStep Start ");
        return stepBuilderFactory.get("threadStep")
                .<Member, Member>chunk(1000)// chunk-size 설정 = Commit Interval
                .reader(pagingItemReader())
                .listener(new CustomItemReadListener())
                .processor((ItemProcessor<Member, Member>) item -> item)
                .listener(new CustomItemProcessorListener())
                .writer(customItemWriter())
                .listener(new CustomItemWriterListener())
                .taskExecutor(taskExecutor())    // 쓰레드를 쓰겠다고 설정하면 됨. (단일 쓰레드 : 소요시간 = 7467) (4쓰레드 : 소요시간 = 5019)
                .build();
    }

    /**
     *  쓰레드 설정
     *
     * */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);                   // 쓰레드 갯수 설정
        taskExecutor.setMaxPoolSize(8);                    // 최대 쓰레드 갯수
        taskExecutor.setThreadNamePrefix("Async-Thread");  // 접두사_쓰레드ID

        return taskExecutor;
    }


    @Bean
    public JdbcPagingItemReader<Member> pagingItemReader() {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(1000);
        reader.setRowMapper(new MemberRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username < :username ");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", 10001);
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
