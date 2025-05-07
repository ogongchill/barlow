package com.barlow.app.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class BatchCoreTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchCoreTestApplication.class, args);
	}
}
