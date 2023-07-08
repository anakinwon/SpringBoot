package com.pisien.springbatch.decider;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class CustomDecider implements JobExecutionDecider {
    // 서버 기동중에 유효한 전역변수
    private int count = 0;

    @Override
    public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
        // 호출될 때 마나 홀수/짝수로 토글해 준다.
        count++;

        if (count %2 ==0)
            return new FlowExecutionStatus("EVEN");
         else
            return new FlowExecutionStatus("ODD");
    }
}
