package com.pisien.springbatch;

import com.pisien.springbatch.config.TestBatchConfig;
import com.pisien.springbatch.partition.PartitionConfiguration;
import com.pisien.springbatch.writer.JdbcDBtoDBConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 배치 테스트 하기
 *   $. 한파일에 한 배치만 테스트 가능하다.
 *   $. 필수 애노테이션
 *      - @SpringBatchTest
 *      - @RunWith(SpringRunner.class)
 *      - @SpringBootTest(classes = { SynchronizedConfiguration.class      <= 테스트 할 배치 프로그램 ID
 *                                  , TestBatchConfig.class                <= 테스트 환경 설정 파일.
 *                                  }
 *                      )
 * */

@SpringBatchTest
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PartitionConfiguration.class, TestBatchConfig.class})
public class SimpleJobTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired private JdbcTemplate jdbcTemplate;

//    @Test
//    public void simpleJobTest() throws Exception {
//        //given
//        JobParameters jobParameters = new JobParametersBuilder()
//                .addString("name","anakin")
//                .addLong("date", new Date().getTime())
//                .toJobParameters();
//
//        // when
//        JobExecution jobExecution  = jobLauncherTestUtils.launchJob(jobParameters);
//
//        // then
//        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
//        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
//    }

    @Test
    public void simpleStepTest() throws Exception {
        //given
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("username",100L)
                .addString("name","anakin")
                .addLong("date", new Date().getTime())
                .toJobParameters();

        // when
        JobExecution jobExecution  = jobLauncherTestUtils.launchJob(jobParameters);
        JobExecution jobExecution2 = jobLauncherTestUtils.launchStep("jdbcBatchStep");

        StepExecution stepExecution = (StepExecution) ((List) jobExecution2.getStepExecutions()).get(0);

        // then
        // 배치 최종상태 코드
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);
        // 배치 종료상태 코드
        assertThat(jobExecution.getExitStatus()).isEqualTo(ExitStatus.COMPLETED);
        // 총 읽은 건 수
        assertThat(stepExecution.getReadCount()).isEqualTo(10000);
        // 총 작성 건 수
        assertThat(stepExecution.getWriteCount()).isEqualTo(10000);
        // 총 커밋 건 수(마지막 +한 건)
        assertThat(stepExecution.getCommitCount()).isEqualTo(11);
    }


}
