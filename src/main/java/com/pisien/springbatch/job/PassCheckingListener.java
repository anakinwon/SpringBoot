package com.pisien.springbatch.job;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

public class PassCheckingListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {}

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String exitCode = stepExecution.getExitStatus().getExitCode();

        // 실패가 아닐경우 사용자 정의 코드를 만들어서 반환한다.
        if(!exitCode.equals(ExitStatus.FAILED.getExitCode())) {
            return new ExitStatus("ANAKIN_CODE");
        }
        return null;
    }
}
