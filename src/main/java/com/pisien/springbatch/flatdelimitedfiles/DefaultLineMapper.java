package com.pisien.springbatch.flatdelimitedfiles;

import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.LineTokenizer;

/**
 *   <직접 코딩 방식>
 *      - 직접 만들어서 사용해 보기.
 *      - FlatFileItemReaderBuilder 이거 사용 시 필요 없음.
 * */
public class DefaultLineMapper<T> implements LineMapper<T> {
    // 두개의 속성이 필요하다. = 1. LineTokenizer +  2. FieldSetMapper<T>
    private LineTokenizer tokenizer;
    private FieldSetMapper<T> fieldSetMapper;

    @Override
    public T mapLine(String line, int lineNumber) throws Exception {
        return fieldSetMapper.mapFieldSet(tokenizer.tokenize(line));
    }

    public void setLineTokenizer(LineTokenizer lineTokenizer) {
        this.tokenizer = lineTokenizer;
    }

    public void setFieldSetMapper(FieldSetMapper<T> fieldSetMapper) {
        this.fieldSetMapper = fieldSetMapper;
    }
}
