package com.pisien.springbatch.skipRetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemWriter implements ItemWriter<String > {
    private final Logger logger = LoggerFactory.getLogger("CustomItemWriter 의 로그");
    private int cnt = 0;

    @Override
    public void write(List<? extends String> items) throws Exception {

        for (String item : items) {
            if (cnt<2) {
                if (cnt%2 ==0) {
                    cnt++;
                }
                else if (cnt%2 ==1) {
                    cnt++;
                    throw new CustomRetryException("failed");
                }
            }
            logger.info("write : " + item);
        }
    }

}
