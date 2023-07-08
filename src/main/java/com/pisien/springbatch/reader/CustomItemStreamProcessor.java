package com.pisien.springbatch.reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class CustomItemStreamProcessor implements ItemProcessor< Customer    // 읽은 Item
                                                         , Customer>   // 보낼 Item
{
    private final Logger logger = LoggerFactory.getLogger("CustomItemProcessor 의 로그");

    @Override
    public Customer process(Customer customer) throws Exception {
        logger.info(" \t\t 2. Processor.item"+ customer);
        // 대문자로 바꾸는 가공 처리를 한다.
        customer.setName(customer.getName().toUpperCase());
        return customer;
    }
}
