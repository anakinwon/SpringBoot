package com.pisien.springbatch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling       // 스케줄러 사용시 등록할 애노테이션.
@SpringBootApplication
@EnableBatchProcessing  // Spring Batch 를 사용하기 위한 필수 애노터이션
public class SpringbatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbatchApplication.class, args);
	}

}
