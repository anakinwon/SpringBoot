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
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.builder.JsonFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

//@Configuration
//@Component
@RequiredArgsConstructor
public class JsonDBtoJSONConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JsonDBtoJSONConfiguration 의 로그");
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    /**
     *   < DB를 읽어서 -> JSon 파일로 만들기>
     *       - D:\JpaNQueryDsl\springbatch\src\main\resources\
     *       - memberWrite.json
     *
     * */
    @Bean
//    @Scheduled(cron="*/20 * * * * *")
    public Job job() {
        logger.info("1. jsonWriter Start ");
        return this.jobBuilderFactory.get("jsonWriterJob")
                .incrementer(new RunIdIncrementer())
                .start(jsonWriterStep())
                .build();
    }

    @Bean
    public Step jsonWriterStep() {
        logger.info("\t2. jsonWriterStep Start ");
        return stepBuilderFactory.get("jsonWriterStep")
                .<Member, Member>chunk(10)// chunk-size 설정 = Commit Interval
                .reader(customItemReader())
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<Member> customItemReader() {
        logger.info("\t\t3. customItemReader Start ");
        JdbcPagingItemReader<Member> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(this.dataSource);
        reader.setFetchSize(10);
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
    public ItemWriter<? super Member> customItemWriter() {
        logger.info("\t\t\t4. customItemWriter Start ");
        return new JsonFileItemWriterBuilder<Member>()
                .name("staxEventWriter")
                .jsonObjectMarshaller(new JacksonJsonObjectMarshaller<>())
                .resource(new FileSystemResource("D:\\JpaNQueryDsl\\springbatch\\src\\main\\resources\\memberWrite.json"))
                .build();
    }

}
