package com.pisien.springbatch.jpa;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
@RequiredArgsConstructor
public class AdapterItemWriterConfiguration {
    private final Logger logger = LoggerFactory.getLogger("AdapterItemWriterConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <기존의 다른 서비스를 Adapter 로 연결 하는 기능>
     *       - reflection 기술 이해 필요.
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("adapterWriterJob")
                .incrementer(new RunIdIncrementer())
                .start(adapterWriterStep())
                .build();
    }

    @Bean
    public Step adapterWriterStep() {
        return stepBuilderFactory.get("adapterWriterStep")
                .<String, String>chunk(10)
                .reader(new ItemReader<String>() {
                    // 테스트를 위한 익명클래스
                    int i = 0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        return i > 10 ? null : "item" + i ;
                    }
                })
                .writer(customItemWriter())
                .build();
    }

    @Bean
    public ItemWriter<? super String> customItemWriter() {

        ItemWriterAdapter<String> writer = new ItemWriterAdapter<>();
        writer.setTargetObject(customService());
        writer.setTargetMethod("customWrite");

        return writer;
    }


    @Bean
    public CustomService customService() {
        return new CustomService();
    }

}
