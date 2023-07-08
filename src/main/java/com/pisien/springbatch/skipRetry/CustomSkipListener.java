package com.pisien.springbatch.skipRetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

public class CustomSkipListener implements SkipListener<Integer, String> {
    private final Logger logger = LoggerFactory.getLogger("CustomSkipListener 의 로그");

    @Override
    public void onSkipInRead(Throwable t) {
        logger.info(">> onSkipInRead : " + t.getMessage());
    }

    @Override
    public void onSkipInWrite(String item, Throwable t) {
        logger.info(">> onSkipInWrite.item    : " + item);
        logger.info(">> onSkipInWrite.message : " + t.getMessage());
    }

    @Override
    public void onSkipInProcess(Integer item, Throwable t) {
        logger.info(">> onSkipInProcess.item    : " + item);
        logger.info(">> onSkipInProcess.message : " + t.getMessage());
    }
}
