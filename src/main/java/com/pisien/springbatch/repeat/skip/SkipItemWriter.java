package com.pisien.springbatch.repeat.skip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class SkipItemWriter implements ItemWriter<String> {
    private final Logger logger = LoggerFactory.getLogger("SkipItemWriter 의 로그");
    private int cnt =0;

    /**
     *  ItemWriter 예외 시 Skip 처리하기.
     * */
    @Override
    public void write(List<? extends String> items) throws Exception {

        for (String item : items) {
            if (item.equals("-12") || item.equals("-14")|| item.equals("-16")) {
                logger.info("\t\t\t\t 4.1 Write Failed  = " + items);
                throw new SkippableException("\t\t\t\t 4.1 Write Failed cnt : " + cnt);
            } else {
                logger.info("\t\t\t\t 4.2 ItemWriter Success = " + items);
            }
        }
    }
}
