package com.batch.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringBatchCsvApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchCsvApplication.class, args);
	}

}
