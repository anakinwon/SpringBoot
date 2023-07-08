package com.pisien.springbatch.processor;


import lombok.Builder;
import lombok.Data;

/**
 *    작업을 분기할 수 있는 기준을 가지고 있는 Bean
 *
 * */
@Data
@Builder
public class ProcessorInfo {

    private int id;

}
