package com.pisien.springbatch.parallel;

import com.pisien.springbatch.config.StopWatchJobListener;
import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.step.tasklet.TaskletStep;
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
public class ParallelConfiguration {
    private final Logger logger = LoggerFactory.getLogger("ParallelConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   <병렬처리 저장하기.>
     *
     * */
    @Bean
    public Job job() throws Exception {
        logger.info("1. parallelBatchJob Start ");
        return this.jobBuilderFactory.get("parallelBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(flow1())
//                .next(flow2())
                .split(taskExecutor()).add(flow2())
                .end()
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Flow flow1() throws Exception {
        TaskletStep step1 = stepBuilderFactory.get("step1")
                .tasklet(tasklet())
                .build();
        return new FlowBuilder<Flow>("flow1")
                .start(step1)
                .build();
    }

    @Bean
    public Flow flow2() throws Exception {
        TaskletStep step2 = stepBuilderFactory.get("step1")
                .tasklet(tasklet())
                .build();
        TaskletStep step3 = stepBuilderFactory.get("step1")
                .tasklet(tasklet())
                .build();
        return new FlowBuilder<Flow>("flow2")
                .start(step2)
                .next(step3)
                .build();
    }

    /**
     * 임시로 만들어 놓은 Tasklet 주입해 온다.
     * */
    @Bean
    public Tasklet tasklet() {
        return new CustomTasklet();
    }

    /**
     *  쓰레드 설정
     *
     * */
    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);                     // 쓰레드 갯수 설정
        taskExecutor.setMaxPoolSize(4);                      // 최대 쓰레드 갯수
        taskExecutor.setThreadNamePrefix("Async-Parallel");  // 접두사_쓰레드ID

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
