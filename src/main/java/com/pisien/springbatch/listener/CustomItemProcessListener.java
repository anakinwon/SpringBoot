package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

/**
 *  <CustomItemProcessListener>
 *      - ItemProcessListener
 *      - @beforeProcess
 *      - @afterProcess
 *
 * */
public class CustomItemProcessListener implements ItemProcessListener {
    private final Logger logger = LoggerFactory.getLogger("CustomItemProcessListener 의 로그");

    @Override
    public void beforeProcess(Object item) {
        logger.info("\t\t\t\t\t5. beforeProcess Listener");
    }

    @Override
    public void afterProcess(Object item, Object result) {
        logger.info("\t\t\t\t\t5. afterProcess Listener");
    }

    @Override
    public void onProcessError(Object item, Exception e) {
        logger.info("\t\t\t\t\t5. onProcessError Listener");
    }

}
