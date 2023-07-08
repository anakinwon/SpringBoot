package com.pisien.springbatch.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

public class CustomItemProcessorListener implements ItemProcessListener<Member, Member> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemProcessorListener 의 로그");
    @Override
    public void beforeProcess(Member item) {
    }

    @Override
    public void afterProcess(Member item, Member result) {
//        logger.info("Thread : " + Thread.currentThread().getName() + ", processor item = " + item.getUsername());
    }

    @Override
    public void onProcessError(Member item, Exception e) {
    }
}
