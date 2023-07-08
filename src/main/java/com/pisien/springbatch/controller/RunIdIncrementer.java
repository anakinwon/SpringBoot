package com.pisien.springbatch.controller;

import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.lang.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class RunIdIncrementer implements JobParametersIncrementer {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
    // 증가될 key 명
    private static String RUN_ID_KEY = "run.id";
    private String key;

    public RunIdIncrementer() {
        this.key = RUN_ID_KEY;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public JobParameters getNext(@Nullable JobParameters parameters) {
        // JobParameters 셋팅
        JobParameters params = parameters == null ? new JobParameters() : parameters;

        // parameters의 증가될 key 로 value 얻기
        JobParameter runIdParameter = (JobParameter)params.getParameters().get(this.key);

        long id = 1L;
        if (runIdParameter != null) {
            try {
                // 기존 값의 + 1
//                String id = format.format(new Date());
                id = Long.parseLong(format.format(new Date())) + 1L;
            } catch (NumberFormatException var7) {
                throw new IllegalArgumentException("Invalid value for parameter " + this.key, var7);
            }
        }

        // 셋팅된 JobParameters로 return
        return (new JobParametersBuilder(params)).addLong(this.key, id).toJobParameters();
    }
}