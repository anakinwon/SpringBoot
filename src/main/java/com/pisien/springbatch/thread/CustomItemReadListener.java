package com.pisien.springbatch.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.core.ItemReadListener;

public class CustomItemReadListener implements ItemReadListener<Member> {
    private final Logger logger = LoggerFactory.getLogger("CustomerItemReadListener 의 로그");

    @Override
    public void beforeRead() {
    }

    @Override
    public void afterRead(Member item) {
//        logger.info("Thread : " + Thread.currentThread().getName() + ", read item = " + item.getUsername());
    }

    @Override
    public void onReadError(Exception ex) {
    }
}
