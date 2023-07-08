package com.pisien.springbatch.reader;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.service.CustomAdapterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

//@Configuration
@RequiredArgsConstructor
public class AdapterItemReaderConfiguration {
    private final Logger logger = LoggerFactory.getLogger("AdapterItemReaderConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;
    private final EntityManagerFactory emf;

    /**
     *   <기존의 다른 서비스를 Adapter 로 연결 하는 기능>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("adapterJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)
                .reader(adapterItemReader())
                .writer(adapterItemWriter())
                .build();
    }

    @Bean
    public ItemReader<String> adapterItemReader() {

        ItemReaderAdapter<String> reader = new ItemReaderAdapter<>();
        reader.setTargetObject(customAdapterService());
        reader.setTargetMethod("customAdapterReader");

        return reader;

    }

    @Bean
    public Object customAdapterService() {
        return new CustomAdapterService();
    }

    @Bean
    public ItemWriter<String> adapterItemWriter() {
        return items -> {
            for (String item : items) {
                System.out.println("item = " + item.toString());
            }
        };
    }
}
