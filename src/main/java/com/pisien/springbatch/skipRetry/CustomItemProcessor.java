package com.pisien.springbatch.skipRetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemProcessor implements ItemProcessor<Integer, String > {
    private final Logger logger = LoggerFactory.getLogger("CustomItemProcessor 의 로그");
    private int cnt = 0;

    @Override
    public String process(Integer item) throws Exception {

        if (cnt<2) {
            if (cnt%2 ==0) {  // 짝수이면 1증가
                cnt++;
            }
            else if (cnt%2 ==1) { // 홀수 이면 1증가하고. 에러 발생
                cnt++;
                throw new CustomRetryException(" CustomItemProcessor failed");
            }
        }
        return String.valueOf(item);
    }

}
