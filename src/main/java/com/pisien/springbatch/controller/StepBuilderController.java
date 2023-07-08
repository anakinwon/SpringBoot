package com.pisien.springbatch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.batch.BasicBatchConfigurer;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class StepBuilderController {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Autowired private Job job;
    // 동기식
    @Autowired private JobLauncher jobLauncher;
    // 비동기 식
    @Autowired private BasicBatchConfigurer basicBatchConfigurer;

    /**
     *  1. WEB API 동기방식 Test 하기.
     *     - 비동기방식 호출 시 5초가 걸린다.
     *     - POST호출 : http://localhost:8080/increment
     *  */
    @PostMapping("/increment")
    @ResponseBody                  // 기본값인 View로 반환하려면 꼭 써줘야 한다.
    public String stepBuilder() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        String id = format.format(new Date());

        JobParameters  jobParameters = new JobParametersBuilder()
                .addString("id", "anakin")
                .addString("date", "20230622")
                .addString("message", "20230622")
                .addString("run.id", id)                       // 재시작을 위한 년월일 시간 파라미터 전송
                .toJobParameters();
        // 비동기 방식 호출하기.
        SimpleJobLauncher jobLauncher2 = (SimpleJobLauncher) basicBatchConfigurer.getJobLauncher();
        jobLauncher2.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher2.run(job, jobParameters);

        return "Async Batch Completed";
    }

}
