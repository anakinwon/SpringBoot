package com.pisien.springbatch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.ArrayList;
import java.util.List;

public class CustomItemReader implements ItemReader<Customer> {
    private final Logger logger = LoggerFactory.getLogger("CustomItemReader 의 로그");

    // 리스트에 아이템을 저장해서 반환한다.
    private List<Customer> list;
    // 생성자를 만들어 반환한다.
    public CustomItemReader(List<Customer> list) {
        this.list = new ArrayList<>(list);
    }

    @Override
    public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        logger.info(" \t 1. Reader.item"+ list);

        if(!list.isEmpty()) {   // 한 건씩 읽고, 삭제한다.
            return list.remove(0);
        }
        return null;  // 더 이상 읽을 데이터가 없을 때 null을 반환한다.
    }
}
