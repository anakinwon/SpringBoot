package com.pisien.springbatch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.*;

import java.util.List;

public class CustomItemStreamReader implements ItemStreamReader<String> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemStreamReader 의 로그");

    private final List<String> items;
    private int index = -1;
    private boolean restart = false;

    public CustomItemStreamReader(List<String> items) {
        this.items = items;
        this.index = 0;
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        logger.info(" \t 1. streamReader.item"+ items);

        String item = null;

        if (this.index< this.items.size()) {
            item = this.items.get(index);
            index++;
        }
        if (this.index==20 && !restart) {
            throw new RuntimeException("Restart is required~~!");
        }

        return item;  // 더 이상 읽을 데이터가 없을 때 null을 반환한다.
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // 초기화
        if (executionContext.containsKey("index")) {
            index = executionContext.getInt("index");
            this.restart = true;
        }
        else {
            index = 0;
            executionContext.put("index", index);
        }

    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        // 청크가 실행될 때 마다 인덱스 상태를 저장한다.
        executionContext.put("index",index);

    }

    @Override
    public void close() throws ItemStreamException {
        // 작업 해제하기
        logger.info(" \t\t\t\t\t All Task was closed");
    }


}
