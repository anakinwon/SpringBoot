package com.pisien.springbatch.scheduler;

import com.pisien.springbatch.writer.JsonDBtoJSONConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//@Component
public class MasterScheduler {
    private final Logger logger = LoggerFactory.getLogger("MasterScheduler 의 로그");
    static final SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");

    @Autowired private JobLauncher jobLauncher;
    @Autowired private Job xMLFileConfiguration;
    @Autowired private Job jsonFileLoadConfiguration;

    @Scheduled(cron="*/5 * * * * *")
    public void scheduler1() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        logger.info("\t\t 5초마다 Schedule1이 동작하고 있습니다.: {}", Calendar.getInstance().getTime());
        String id = format.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", "anakin")
                .addString("date", "20230622")
                .addString("message", "20230622")
                .addString("run.id", id)                       // 재시작을 위한 년월일 시간 파라미터 전송
                .toJobParameters();

        jobLauncher.run(xMLFileConfiguration, jobParameters);

    }

    @Scheduled(cron="*/7 * * * * *")
    public void scheduler2() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

        logger.info("\t\t 7초마다 Schedule2이 동작하고 있습니다.: {}", Calendar.getInstance().getTime());
        String id = format.format(new Date());

        JobParameters jobParameters = new JobParametersBuilder()
                .addString("id", "anakin")
                .addString("date", "20230622")
                .addString("message", "20230622")
                .addString("run.id", id)                       // 재시작을 위한 년월일 시간 파라미터 전송
                .toJobParameters();

        jobLauncher.run(jsonFileLoadConfiguration, jobParameters);


    }

}
