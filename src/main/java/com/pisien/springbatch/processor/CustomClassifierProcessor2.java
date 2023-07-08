package com.pisien.springbatch.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomClassifierProcessor2 implements ItemProcessor<ProcessorInfo, ProcessorInfo> {
    private final Logger logger = LoggerFactory.getLogger("CustomClassifierProcessor2 의 로그");

    @Override
    public ProcessorInfo process(ProcessorInfo item) throws Exception {
        logger.info("\t\t 2-2. CustomClassifierProcessor2 was executed~~!");
        return item;
    }
}
