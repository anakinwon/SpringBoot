package com.pisien.springbatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor2 implements ItemProcessor<String, String> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemProcessor2 의 로그");
    int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        cnt++;

        logger.info("\t\t 2-2. CustomItemProcessor2 was executed~~!");

        return item + cnt;
    }


}
