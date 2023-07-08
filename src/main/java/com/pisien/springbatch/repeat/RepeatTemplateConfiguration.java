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
 * Repeat 를 통해서 반복작업을 수행한다.
 *
 * */

//@Configuration
@RequiredArgsConstructor
public class RepeatTemplateConfiguration {
    private final Logger logger = LoggerFactory.getLogger("RepeatConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. repeatJob~!");
        return jobBuilderFactory.get("repeatJob")
                .incrementer(new RunIdIncrementer())
                .start(repeatStep())
                .build();
    }

    @Bean
    public Step repeatStep() {
        logger.info("\t 2. repeatStep Started~!");
        return stepBuilderFactory.get("repeatStep")
                .<String, String>chunk(5)
                .reader(new ItemReader<>() {
                    int i = 0;
                    @Override
                    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        return i > 5 ? null : "item" + i;
                    }
                })
                .processor(new ItemProcessor<String, String>() {

                    RepeatTemplate repeatTemplate = new RepeatTemplate();

                    @Override
                    public String process(String item) throws Exception {

                        /**
                         *  <선택적 복합 반복문> - (AND 조건)
                         *      - SimpleCompletionPolicy
                         *      - 5회가 되먼 종료되는데,
                         *      - 7초가 되면 먼저 종료해라
                         * */
                        //repeatTemplate.setCompletionPolicy(new SimpleCompletionPolicy(5));           // 5번 반복한다.
                        //repeatTemplate.setCompletionPolicy(new TimeoutTerminationPolicy(7000));      // 5번 반복하는 동안 7초가 넘으면 자동 Skip

                        /**
                         *  <선택적 복합 반복문> - (OR 조건)
                         *      - CompletionPolicy
                         *      - 3회가 되먼 종료되거나,
                         *      - 5초가 먼저 끝나면 반복 종료해라
                         * */
                        CompositeCompletionPolicy compositePolicy = new CompositeCompletionPolicy();
                        CompletionPolicy[] completionPolicies = new CompletionPolicy[] {
                                new SimpleCompletionPolicy(5)     // 5회가 먼저 끝나거나
                                , new TimeoutTerminationPolicy(7000)       // 7초가 먼저 끝나면 반복 종료해라
                        };
                        compositePolicy.setPolicies(completionPolicies);
                        repeatTemplate.setCompletionPolicy(compositePolicy);

                        /**
                         *  예외가 발생하더라도, 계속 수행되는 횟수를 설정한다.
                         *  예외 오류 최대 2회까지 반복 수행
                         * */
                        repeatTemplate.setExceptionHandler(SimpleLimitHandler());

                        repeatTemplate.iterate(new RepeatCallback() {
                            int j = 1;
                            @Override
                            public RepeatStatus doInIteration(RepeatContext context) throws Exception {
                                // 여기에 Base Logic 구현 (총 9번 repeat)
                                logger.info("Repeat Testing is here = [" + j++ +"]");
                                // 2초씩 Delay 시키기다.
                                Thread.sleep(2000);
                                throw new RuntimeException("Executed Error is Occurred~~!["+j+"] times");
                                //return RepeatStatus.CONTINUABLE;
                            }
                        });

                        return item;
                    }
                })
                .writer(items -> logger.info("\t\t\t 3. items = " + items))
                .build();
    }

    /**
     * 예외 발생 시 반복문을 종료시키는 메소드
     * 
     * */
    @Bean
    public ExceptionHandler SimpleLimitHandler() {
        return new SimpleLimitExceptionHandler(3);

    }
}

