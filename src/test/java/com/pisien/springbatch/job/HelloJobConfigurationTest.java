package com.pisien.springbatch.job;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class HelloJobConfigurationTest {

    @Autowired
    private HelloJobConfiguration helloJobConfiguration;

    @Autowired
    private GoodDayJobConfiguration goodDayJobConfiguration;

    @Test
    void helloJop() {

        helloJobConfiguration.helloJob();
        goodDayJobConfiguration.goodDayJop();

    }
}