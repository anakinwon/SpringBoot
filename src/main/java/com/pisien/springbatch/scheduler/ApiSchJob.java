package com.pisien.springbatch.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

//@Component
public class ApiSchJob extends QuartzJobBean {
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Autowired private Job apiJob;
    @Autowired private JobLauncher jobLauncher;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        String id = format.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", "anakin")
                .addString("date", "20230622")
                .addString("message", "20230622")
                .addString("run.id", id)                       // 재시작을 위한 년월일 시간 파라미터 전송
                .toJobParameters();

        try {
            jobLauncher.run(apiJob, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new RuntimeException(e);
        } catch (JobRestartException e) {
            throw new RuntimeException(e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        } catch (JobParametersInvalidException e) {
            throw new RuntimeException(e);
        }
    }
}
