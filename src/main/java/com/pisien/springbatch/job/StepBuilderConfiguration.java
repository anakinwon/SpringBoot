package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import lombok.RequiredArgsConstructor;
import org.aspectj.runtime.internal.CFlowStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


/**
 *   Incrementer
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class StepBuilderConfiguration {
    private final Logger logger = LoggerFactory.getLogger("StepBuilderConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return this.jobBuilderFactory.get("batchJob")
                .incrementer(new RunIdIncrementer())     // API 호출 시 무용지물임.
                .start(step1())
                .next(step2())
                .next(step3())
                .next(step4())
                .build();
    }

    /**
     *  Step1 생성
     *
     * */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step1")
                .tasklet((contribution, chunkContext) -> {
                    System.out.println(" \t step1 has Executed!!");
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

    /**
     *  Step2 생성
     *
     * */
    @Bean
    public Step step2() {
        System.out.println(" \t step2 has Executed!!");
        return stepBuilderFactory.get("step2")
                .<String, String>chunk(3)
                .reader(new ItemReader<String>() {  // Extract
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        return null;
                    }
                })
                .processor(new ItemProcessor<String, String>() {   // Transform
                    @Override
                    public String process(String item) throws Exception {
                        return null;
                    }
                })
                .writer(new ItemWriter<String>() {   // Loader
                    @Override
                    public void write(List<? extends String> items) throws Exception {

                    }
                })
                .build();
    }

    /**
     *  Step3 생성
     *
     * */
    @Bean
    public Step step3() {
        System.out.println(" \t step3 has Executed!!");
        return stepBuilderFactory.get("step3")
                .partitioner(step1())
                .gridSize(2)
                .build();
    }

    /**
     *  Step 내의 Job 호출하기.
     *
     * */
    @Bean
    public Step step4() {
        System.out.println(" \t step4 has Executed!!");
        return stepBuilderFactory.get("step4")
                .job(job())
                .build();
    }

    /**
     *  Flow 호출하기
     *
     * */
    @Bean
    public Step step5() {
        System.out.println(" \t step5 has Executed!!");
        return stepBuilderFactory.get("step5")
                .flow(flow())
                .build();
    }

    /**
     *  Step 내의 Job 생성
     *
     * */
    @Bean
    public Job job() {
        return this.jobBuilderFactory.get("job")
                .start(step1())
                .next(step2())
//                .next(step3())
                .build();
    }

    /**
     *  Flow 생성
     *
     * */
    @Bean
    public Flow flow() {
        FlowBuilder<Flow> flowBuilder = new FlowBuilder<>("flow");
        flowBuilder
                .start(step2())
                .end();

        return flowBuilder.build();
    }

}
