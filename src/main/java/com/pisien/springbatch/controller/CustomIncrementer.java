package com.pisien.springbatch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * incrementer 구현
 */
public class CustomIncrementer implements JobParametersIncrementer {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

    /**
     * Date 를 incrementer 로 지정해보자.
     * @param jobParameters
     * @return
     */
    @Override
    public JobParameters getNext(JobParameters jobParameters) {
        // 해당 날짜-시간으로 설정
        String id = format.format(new Date());

        return new JobParametersBuilder().addString("run.id", id).toJobParameters();
    }
}
