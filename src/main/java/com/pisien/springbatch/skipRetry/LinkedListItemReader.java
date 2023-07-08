package com.pisien.springbatch.skipRetry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.lang.Nullable;

import java.util.LinkedList;
import java.util.List;

public class LinkedListItemReader<T> implements ItemReader<T> {
    private final Logger logger = LoggerFactory.getLogger("SkipListenerConfiguration 의 로그");

    private List<T> list;

    public LinkedListItemReader(List<T> list) {
        if(AopUtils.isAopProxy(list)) {
            this.list = list;
        }
        else {
            this.list = new LinkedList<>(list);
        }
    }

    @Nullable
    @Override
    public T read() throws CustomSkipException {

        if (!list.isEmpty()) {
            T remove = (T)list.remove(0);
            if ((Integer)remove == 3) {
                throw new CustomSkipException("Read Skipped : " + remove);
            }
            logger.info("Read item = " + remove);
            return remove;
        }
        return null;
    }
}
