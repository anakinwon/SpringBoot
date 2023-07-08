package com.pisien.springbatch.job;

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
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

//@Configuration
@RequiredArgsConstructor
public class ChunkProviderConfiguration {
    private final Logger logger = LoggerFactory.getLogger("BatchStatusExitStatusConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     *   <Flow Step 구성하기>
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("statusJob")
                .incrementer(new RunIdIncrementer())
                .start(step1())
                .next(step2())
                .build();
    }

    /**
     * <청크 기반 테스트하기>
     *     1. ListItemReader    : E-Extract
     *       > 2. ItemProcessor : T-Transform
     *          > 3. ItemWriter : L-Load
     * */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .<String, String>chunk(10)// chunk-size 설정 = Commit Interval
                .reader(new ListItemReader<>(     // (1). ListItemReader  (청크의 필수 기능)
                            Arrays.asList("item1","item2","item3","item4","item5"
                                ,"item6","item7","item8","item9","item10"
                                ,"item11","item12","item13","item14","item15"
                                ,"item16","item17","item18","item19","item10"
                            )
                        )
                )
                .processor(new ItemProcessor<String, String>() {
                    @Override                     // (2).ItemProcessor (청크의 옵션 기능)
                    public String process(String item) throws Exception {
                        logger.info("\t\t 1초 마다 아이템 하나씩 읽어 온다. = " + item);
                        Thread.sleep(1000);
                        return item;
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override                     // (3).ItemWriter (청크의 필수 기능)
                    public void write(List<? extends String> items) throws Exception {
                        logger.info(" \t\t\t 10초 마다 청크(10개 아이템) 를 쓰기한다. = " + items);
                        Thread.sleep(2000);
                    }
                })
                .build();
    }
    @Bean
    public Step step2() {
        return stepBuilderFactory.get("step2")
                .tasklet((contribution, chunkContext) -> {
                    logger.info(" step2 was Executed~!");
                    return RepeatStatus.FINISHED;
                })  //익명 클래스 사용
                .build();
    }

}
