package com.pisien.springbatch.job;

import com.pisien.springbatch.controller.RunIdIncrementer;
import com.pisien.springbatch.tasklet.CustomTasklet3;
import com.pisien.springbatch.tasklet.TestTasklet;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.job.DefaultJobParametersExtractor;
import org.springframework.batch.core.step.job.JobParametersExtractor;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;


/**
 *   JobStep
 *
 * */
//@Configuration
@RequiredArgsConstructor
public class JobStepConfiguration {
    private final Logger logger = LoggerFactory.getLogger("JobStepConfiguration 의 로그");

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job batchJob() {
        return this.jobBuilderFactory.get("jobStep")
                .incrementer(new RunIdIncrementer())     // API 호출 시 무용지물임.
                .start(taskStep())
                .next(step1())
                .next(jobStep(null))
                .next(step2())                           // step2 강제 오류 발생됨.
                .next(chunkStep())                       // step2 오류로 실행 안됨.
                .build();
    }

    @Bean
    public Step taskStep() {
        return stepBuilderFactory.get("taskStep")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        logger.info(" step was Executed~! ");
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();

    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("taskStep1")
                .tasklet(new TestTasklet())  // 별도 클래스 호출
                .allowStartIfComplete(true)  // 무조건 수동재실행 가능 옵션
                .build();

    }

    @Bean
    public Step jobStep(JobLauncher jobLauncher) {
        return stepBuilderFactory.get("jobStep")
                .job(childJob())
                .launcher(jobLauncher)
                .parametersExtractor(jobParametersExtractor())
                .build();
    }

    private DefaultJobParametersExtractor jobParametersExtractor() {
        DefaultJobParametersExtractor extractor = new DefaultJobParametersExtractor();
        extractor.setKeys(new String[]{"name"});
        return extractor;
    }

    private Job childJob() {
        return this.jobBuilderFactory.get("childJob")
                .start(step3())
                .build();
    }


    @Bean
    public Step step2() {
        return stepBuilderFactory.get("taskStep2")
                .tasklet(new Tasklet() {  // 익명 클래스 사용
                    @Override
                    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
                        throw new RuntimeException("오류 발생");
//                        return RepeatStatus.FINISHED;
                    }
                })
                .startLimit(3)             // 수동재실행할 수 있는 회수 제한. (Maximum start limit exceeded for step: taskStep2StartMax: 3)
                .build();

    }

    @Bean
    public Step step3() {
        return stepBuilderFactory.get("step3")
                .tasklet(new CustomTasklet3() {
                })
                .build();
    }

    @Bean
    public Step chunkStep() {
        return stepBuilderFactory.get("chunkStep")
                .<String, String>chunk(10)
                .reader(new ListItemReader<>(Arrays.asList("item1","item2","item3","item4","item5")))
                .processor(new ItemProcessor<String, String>() {
                    @Override
                    public String process(String item) throws Exception {
                        return item.toUpperCase();
                    }
                })
                .writer(new ItemWriter<String>() {
                    @Override
                    public void write(List<? extends String> items) throws Exception {
                        items.forEach(item -> logger.info("item = " + item));
                    }
                })
                .build();
    }

}
