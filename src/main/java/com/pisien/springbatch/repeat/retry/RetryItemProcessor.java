package com.pisien.springbatch.repeat.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class RetryItemProcessor implements ItemProcessor<String, String> {
    private final Logger logger = LoggerFactory.getLogger("RetryItemProcessor 의 로그");
    private int cnt = 0;

    @Override
    public String process(String item) throws Exception {
        cnt++;

        // Skip 을 사용하지 않으면 계속 반복된다.
        if ( item.equals("2") || item.equals("4") || item.equals("6") ) {
//            logger.info("\t\t2. RetryItemProcessor Fail Count = " + cnt);
            Thread.sleep(1000);   // 1초 지연
            throw new RetryableException();
        }else {
//            logger.info("\t\t2. RetryItemProcessor Retry Count = " + cnt);
            Thread.sleep(1000);   // 1초 지연
            return item;
        }
    }
}
