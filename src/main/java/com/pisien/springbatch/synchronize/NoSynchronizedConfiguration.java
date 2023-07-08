package com.pisien.springbatch.synchronize;

import com.pisien.springbatch.config.StopWatchJobListener;
import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

//@Configuration
@RequiredArgsConstructor
public class NoSynchronizedConfiguration {
    private final Logger logger = LoggerFactory.getLogger("SynchronizedConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   < Synchronized -> DB로 저장하기.>
     *       - PK-ID 중복오류남..
     *       - Unexpected cursor position change. 오류남
     * */
    @Bean
    public Job job() throws Exception{
        logger.info("1. synchronizedBatchJob Start ");
        return this.jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step step1() throws Exception{
        logger.info("\t2. step1 Start ");
        return stepBuilderFactory.get("step1")
                .<Member, Member>chunk(10)
                .reader(customItemReader())
                .listener(new ItemReadListener<Member>() {
                    @Override
                    public void beforeRead() {                    }

                    @Override
                    public void afterRead(Member item) {
                        logger.info("ThreadName = ["+ Thread.currentThread().getName() +"], item.getUsername = " + item.getUsername());
                    }

                    @Override
                    public void onReadError(Exception ex) {                    }
                })
                .writer(customItemWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<Member> customItemReader() {
        logger.info("\t\t3. customItemReader Start ");

        return new JdbcCursorItemReaderBuilder<Member>()
                .fetchSize(10)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Member.class))
                .sql(" select username, password, created_dt from member where username < 51 ")
                .name("Synchronized-Thread")
                .build()
                ;

    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<Member> customItemWriter() {
        logger.info("\t\t\t4. customItemWriter Start ");

        JdbcBatchItemWriter<Member> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql(" Insert into person (password) values (:password) ");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        itemWriter.afterPropertiesSet();

        return itemWriter;
    }

    /**
     *  쓰레드 설정
     * */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(4);                          // 쓰레드 갯수 설정
        taskExecutor.setMaxPoolSize(8);                           // 최대 쓰레드 갯수
        taskExecutor.setThreadNamePrefix("Synchronized-Thread");  // 접두사_쓰레드ID

        return taskExecutor;
    }

}
