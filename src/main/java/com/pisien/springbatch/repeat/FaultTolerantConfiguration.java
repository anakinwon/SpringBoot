package com.pisien.springbatch.repeat;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.repeat.CompletionPolicy;
import org.springframework.batch.repeat.RepeatCallback;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.batch.repeat.exception.ExceptionHandler;
import org.springframework.batch.repeat.exception.SimpleLimitExceptionHandler;
import org.springframework.batch.repeat.policy.CompositeCompletionPolicy;
import org.springframework.batch.repeat.policy.SimpleCompletionPolicy;
import org.springframework.batch.repeat.policy.TimeoutTerminationPolicy;
import org.springframework.batch.repeat.support.RepeatTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *     <FaultTolerantStepBuilder>를 통해서 Retry 또는 Skip을 수행한다.
 *         - Tolerant : 관대한, 아량있는, 내성이 있는, 잘 견디는
 *         - Retry
 *         - Skip
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class FaultTolerantConfiguration {
    private final Logger logger = LoggerFactory.getLogger("FaultTolerantConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. faultTolerant~!");
        return jobBuilderFactory.get("faultTolerantJob")
                .incrementer(new RunIdIncrementer())
                .start(faultTolerantStep())
                .build();
    }

    @Bean
    public Step faultTolerantStep() {
        logger.info("\t 2. faultTolerantStep Started~!");
        return stepBuilderFactory.get("faultTolerantStep")
                .<String, String>chunk(5)
                .reader(new ItemReader<>() {
                    int i = 0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        Thread.sleep(2000);   // 2초 지연
                        i++;
                        if (i==1) {
                            throw new IllegalArgumentException("This Exception is skiped");
                        }
                        return i > 5 ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {
                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {
                        Thread.sleep(2000);   // 2초 지연
                        throw new IllegalArgumentException("This Exception is retried~!");
                    }
                })
                .writer(items -> logger.info("\t\t\t 3. items = " + items))
                .faultTolerant()                       // faultTolerant Start ==========================================
                .skip(IllegalArgumentException.class)  // skip 오류 메시지
                .skipLimit(2)                          // skip 횟수 정의
                .retry(IllegalArgumentException.class) // retry 오류 메시지
                .retryLimit(2)                         // retry 횟수 정의 faultTolerant End =============================
                .build();
    }

    /**
     * 예외 발생 시 반복문을 종료시키는 메소드
     * 
     * */
    @Bean
    public ExceptionHandler SimpleLimitHandler() {
        return new SimpleLimitExceptionHandler(2);

    }
}

