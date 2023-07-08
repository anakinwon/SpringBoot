package com.pisien.springbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;


/**
 *  <청크 리스너>
 *      - CustomChunkListener
 *      - @BeforeJob
 *      - @AfterJob
 *
 * */
public class CustomChunkListener implements ChunkListener {
    private final Logger logger = LoggerFactory.getLogger("CustomChunkListener 의 로그");

    @Override
    public void beforeChunk(ChunkContext context) {
        logger.info("\t\t\t3. beforeChunk Listener");
    }

    @Override
    public void afterChunk(ChunkContext context) {
        logger.info("\t\t\t3. afterChunk Listener");
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        logger.info("\t\t\t3. afterChunkError Listener");
    }
}
