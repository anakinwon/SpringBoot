package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

/**
 *  <CustomItemReadListener>
 *      - ItemReadListener
 *      - @beforeRead
 *      - @afterRead
 *
 * */
public class CustomItemReadListener implements ItemReadListener {
    private final Logger logger = LoggerFactory.getLogger("CustomItemReadListener 의 로그");

    @Override
    public void beforeRead() {
        logger.info("\t\t\t\t4. beforeRead Listener");
    }

    @Override
    public void afterRead(Object item) {
        logger.info("\t\t\t\t4. afterRead Listener");
    }

    @Override
    public void onReadError(Exception ex) {
        logger.info("\t\t\t\t4. onReadError Listener");
    }

}
