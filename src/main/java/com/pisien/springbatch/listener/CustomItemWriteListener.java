package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import java.util.List;

/**
 *  <CustomItemWriteListener>
 *      - ItemWriteListener
 *      - @beforeWrite
 *      - @afterWrite
 *
 * */
public class CustomItemWriteListener implements ItemWriteListener {
    private final Logger logger = LoggerFactory.getLogger("CustomItemWriteListener 의 로그");

    @Override
    public void beforeWrite(List items) {
        logger.info("\t\t\t\t\t\t6. beforeWrite Listener");
    }

    @Override
    public void afterWrite(List items) {
        logger.info("\t\t\t\t\t\t6. afterWrite Listener");
    }

    @Override
    public void onWriteError(Exception exception, List items) {
        logger.info("\t\t\t\t\t\t6. onWriteError Listener");
    }


}
