package com.pisien.springbatch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;

import java.util.ArrayList;
import java.util.List;

public class CustomItemWriter implements ItemWriter<Customer> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemWriter 의 로그");

    @Override
    public void write(List<? extends Customer> items) throws Exception {
        items.forEach(item -> logger.info(" \t\t\t 3. write.item = " + item));
    }
}
