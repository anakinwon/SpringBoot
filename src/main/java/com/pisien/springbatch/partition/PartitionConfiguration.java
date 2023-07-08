package com.pisien.springbatch.partition;

import com.pisien.springbatch.config.StopWatchJobListener;
import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.parallel.Member;
import com.pisien.springbatch.parallel.MemberRowMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
@RequiredArgsConstructor
public class PartitionConfiguration {
    private final Logger logger = LoggerFactory.getLogger("PartitionConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   <파티션처리 저장하기.>
     *
     * */
    @Bean
    public Job job() throws Exception {
        logger.info("1. partitionBatchJob Start ");
        return this.jobBuilderFactory.get("partitionBatchJob")
                .incrementer(new RunIdIncrementer())
                .start(masterStep())
                .listener(new StopWatchJobListener())
                .build();
    }

    /**
     * 마스터 스텝
     * */
    @Bean
    @JobScope
    public Step masterStep() {

        return stepBuilderFactory.get("masterStep")
                .partitioner(slaveStep().getName(), partitioner())
                .step(slaveStep())
                .gridSize(4)
                .taskExecutor(new SimpleAsyncTaskExecutor())
                .build();
    }

    /**
     *  슬레이브 스텝
     *
     * */
    @Bean
    public Step slaveStep() {
        return stepBuilderFactory.get("slaverStep")
                .<Member, Member>chunk(10000)
                .reader(pagingItemReader(null,null))
                .writer(customItemWriter())
                .build();
    }
    @Bean
    public Partitioner partitioner() {
        ColumnRangePartitioner columnRangePartitioner = new ColumnRangePartitioner();

        columnRangePartitioner.setColumn("username");
        columnRangePartitioner.setDataSource(dataSource);
        columnRangePartitioner.setTable("member");

        return columnRangePartitioner;
    }

    @Bean
    @StepScope
    public JdbcPagingItemReader<Member> pagingItemReader(
            @Value("#{stepExecutionContext['minValue']}") Long minValue,
            @Value("#{stepExecutionContext['maxValue']}") Long maxValue
    ) {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10000);
        reader.setRowMapper(new MemberRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(" username, password, created_dt ");
        queryProvider.setFromClause(" from member ");
        queryProvider.setWhereClause(" where username >= :minValue and username < :maxValue");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("minValue", minValue);
        parameters.put("maxValue", maxValue);
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
