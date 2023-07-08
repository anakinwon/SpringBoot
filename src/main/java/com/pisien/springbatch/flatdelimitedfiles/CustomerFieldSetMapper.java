package com.pisien.springbatch.flatdelimitedfiles;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException {

        if(fieldSet == null) {
            return null;
        }

        Customer customer = new Customer();

        // 인덱스로 가져오는 방식
        customer.setName(fieldSet.readString(0));
        customer.setAge(fieldSet.readInt(1));
        customer.setYear(fieldSet.readString(2));

        // 이름으로 가져오는 방식
        customer.setName(fieldSet.readString("name"));
        customer.setAge(fieldSet.readInt("age"));
        customer.setYear(fieldSet.readString("year"));

        return customer;
    }
}
