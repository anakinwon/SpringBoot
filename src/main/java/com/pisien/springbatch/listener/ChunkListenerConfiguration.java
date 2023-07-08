package com.pisien.springbatch.listener;

import com.pisien.springbatch.config.StopWatchJobListener;
import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.skipRetry.CustomSkipException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@Slf4j
//@Configuration
@RequiredArgsConstructor
public class ChunkListenerConfiguration {
    private final Logger logger = LoggerFactory.getLogger("ChunkListenerConfiguration 의 로그");
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final CustomStepExecutionListener customStepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .listener(new StopWatchJobListener())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1")
                .<Integer, String>chunk(10)
                .listener(new CustomChunkListener())
                .listener(new CustomItemReadListener())
                .reader(listItemReader())
                .listener(new CustomItemProcessListener())
                .processor(new ItemProcessor<Integer,String>() {
                    @Override
                    public String process(Integer item) throws Exception {
                        if(item==4) {
                            throw new CustomSkipException("process Skipped~~!");
                        }
                        return "item = " + item;
                    }
                })
                .listener(new CustomItemWriteListener())
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        for (String item : items) {
                            if(item.equals("item5")) {
                                throw new CustomSkipException("writer Skipped~~!");
                            }
                            System.out.println("item = " + item);
                        }
                    }
                })
                .build();
    }

    @Bean
    public ItemReader<Integer> listItemReader() {
        List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
        return new ListItemReader<>(list);
    }

}
