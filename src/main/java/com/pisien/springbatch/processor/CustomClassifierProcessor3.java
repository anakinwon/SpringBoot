package com.pisien.springbatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomClassifierProcessor3 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    private final Logger logger = LoggerFactory.getLogger("CustomClassifierProcessor3 의 로그");

    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        logger.info("\t\t 2-3. CustomClassifierProcessor3 was executed~~!");
        return item;
    }
}
