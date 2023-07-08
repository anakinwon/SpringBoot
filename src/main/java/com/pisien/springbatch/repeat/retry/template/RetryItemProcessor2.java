package com.pisien.springbatch.repeat.retry.template;

import com.pisien.springbatch.repeat.retry.RetryableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.classify.BinaryExceptionClassifier;
import org.springframework.classify.Classifier;
import org.springframework.retry.RecoveryCallback;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.support.DefaultRetryState;
import org.springframework.retry.support.RetryTemplate;

public class RetryItemProcessor2 implements ItemProcessor<String, Customer> {
    private final Logger logger = LoggerFactory.getLogger("RetryItemProcessor 의 로그");
    private int cnt = 0;

    @Autowired
    private RetryTemplate retryTemplate;

    @Override
    public Customer process(String item) throws Exception {

        // Skip을 설정하지 않으면 Retry를 진행하지 못하게 설정하기.
        Classifier<Throwable, Boolean> rollbackClassifier = new BinaryExceptionClassifier(true);

        Customer customer = retryTemplate.execute(
                new RetryCallback<Customer, RuntimeException>( ) {
                    @Override
                    public Customer doWithRetry(RetryContext context) throws RuntimeException {

                        if(item.equals("1") || item.equals("2")) {
                            cnt++;
                            throw new RetryableException("Failed cnt : " + cnt);
                        }
                        return new Customer(item);
                    }
                },
                new RecoveryCallback<Customer>() {
                    @Override
                    public Customer recover(RetryContext context) throws Exception {
                        return new Customer(item);
                    }
                //});
                }, new DefaultRetryState(item, rollbackClassifier));
        return customer;
    }
}
