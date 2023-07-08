package com.pisien.springbatch.repeat.skip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class SkipItemProcessor implements ItemProcessor<String, String> {
    private final Logger logger = LoggerFactory.getLogger("SkipItemProcessor 의 로그");
    private int cnt =0;

    /**
     *  process 중 Skip 된 건수 확인.
     * */
    @Override
    public String process(String item) throws Exception {
        if ( item.equals("2")
                || item.equals("4")
                || item.equals("6")
                || item.equals("8")
                || item.equals("11")
                || item.equals("13")
                || item.equals("15") ) {
            logger.info("\t\t\t 3.1 ItemProcessor Failed : " + item);
            throw new SkippableException("\t\t\t 3.1 Process Failed : "+ cnt);
        }
        else {
            logger.info("\t\t\t 3.2 ItemProcessor Success = " + item);
            return String.valueOf(Integer.valueOf(item) * -1);
        }
    }
}
