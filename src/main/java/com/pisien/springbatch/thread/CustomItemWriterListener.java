package com.pisien.springbatch.thread;

import com.pisien.springbatch.reader.CustomItemWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

public class CustomItemWriterListener implements ItemWriteListener<Member> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemWriterListener 의 로그");
    @Override
    public void beforeWrite(List<? extends Member> items) {
    }

    @Override
    public void afterWrite(List<? extends Member> items) {
//        logger.info("Thread : " + Thread.currentThread().getName() + ", write items = " + items.size());
    }

    @Override
    public void onWriteError(Exception exception, List<? extends Member> items) {
    }
}
