package com.pisien.springbatch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class CustomItemStreamWriter implements ItemStreamWriter<String> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemStreamWriter 의 로그");

    @Override
    public void write(List<? extends String> items) throws Exception {
        items.forEach(item -> logger.info(" \t\t\t 3. CustomItemStreamWriter.item = "+item));
    }
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        logger.info("CustomItemStreamWriter.open()");
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        logger.info("CustomItemStreamWriter.update()");
    }

    @Override
    public void close() throws ItemStreamException {
        logger.info("CustomItemStreamWriter.close()");
    }

}
