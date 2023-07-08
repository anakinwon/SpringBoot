package com.pisien.springbatch.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JobParamRunner implements ApplicationRunner {
    @Autowired private JobLauncher jobLauncher;
    @Autowired private Job job;

    @Override
    public void run(ApplicationArguments args) throws Exception {

//        String dtFormat = new SimpleDateFormat("yyyyMMdd").toString();
//
//        //파라미터 설정
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("requestDate", "20230629") // 문자열 매개변수
//                .addLong("seq", 1009L)                // Long 매개변수
//                .addString("createDt", dtFormat )           // 날짜 매개변수
//                .addDouble("pi", 3.141952)         // 소수점 매개변수
//                .toJobParameters();
//
//        jobLauncher.run(job, jobParameters);
    }

}
