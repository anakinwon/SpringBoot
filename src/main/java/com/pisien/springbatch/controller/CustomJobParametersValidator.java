package com.pisien.springbatch.controller;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

public class CustomJobParametersValidator implements JobParametersValidator {
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters.getString("id")==null || parameters.getString("id").trim().length()<0) {
            throw new JobParametersInvalidException("ID-Parameter is not found~!");
        }

    }
}
