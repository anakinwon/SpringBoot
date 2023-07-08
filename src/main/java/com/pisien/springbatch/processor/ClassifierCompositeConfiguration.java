package com.pisien.springbatch.processor;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.*;
import org.springframework.batch.item.support.ClassifierCompositeItemProcessor;
import org.springframework.batch.item.support.builder.CompositeItemProcessorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *   코드등으로 분류하여 작업을 체이닝한다.
 *      - 체이닝으로 연결해서 처리하는 로직
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class ClassifierCompositeConfiguration {
    private final Logger logger = LoggerFactory.getLogger("ClassifierCompositeConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job job() throws Exception {
        logger.info("1. classifierJob Started~!");
        return jobBuilderFactory.get("classifierJob")
                .incrementer(new RunIdIncrementer())
                .start(classifierStep())
                .build();
    }

    @Bean
    public Step classifierStep() {
        logger.info("\t 2. classifierStep Started~!");
        return stepBuilderFactory.get("classifierStep")
                .<ProcessorInfo, ProcessorInfo>chunk(10)
                .reader(new ItemReader<ProcessorInfo>() {
                    int i = 0;

                    @Override
                    public ProcessorInfo read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                        i++;
                        ProcessorInfo processorInfo = ProcessorInfo.builder().id(i).build();

                        return i>3 ? null : processorInfo;
                    }
                })
                .processor(customItemProcessor())
                .writer(items -> logger.info("\t\t\t 3. items = " + items))
                .build();
    }


    /**
     * Classifier 를 통해서 작업을 선택한다.
     *
     * */

    @Bean
    public ItemProcessor<? super ProcessorInfo, ? extends ProcessorInfo> customItemProcessor() {

        ClassifierCompositeItemProcessor<ProcessorInfo, ProcessorInfo> processor = new ClassifierCompositeItemProcessor<>();
        ProcessorClassifier<ProcessorInfo, ItemProcessor<?,? extends ProcessorInfo>> classifier = new ProcessorClassifier<>();
        Map<Integer, ItemProcessor<ProcessorInfo, ProcessorInfo>> processorMap = new HashMap<>();

        processorMap.put(1, new CustomClassifierProcessor1());
        processorMap.put(2, new CustomClassifierProcessor2());
        processorMap.put(3, new CustomClassifierProcessor3());

        classifier.setProcessorMap(processorMap);
        processor.setClassifier(classifier);

        return processor;
    }


}
