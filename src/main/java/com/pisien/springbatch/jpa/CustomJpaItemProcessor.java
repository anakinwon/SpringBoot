package com.pisien.springbatch.jpa;

import org.modelmapper.ModelMapper;
import org.springframework.batch.item.ItemProcessor;

public class CustomJpaItemProcessor implements ItemProcessor<Member, Person> {
    ModelMapper modelMapper = new ModelMapper();

    @Override
    public Person process(Member item) throws Exception {
        // 모델 매퍼를 통해서, Member->Person 으로 바로 매핑해 준다.
        Person person = modelMapper.map(item, Person.class);

        return person;
    }
}
