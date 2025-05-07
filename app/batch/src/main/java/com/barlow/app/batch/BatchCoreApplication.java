package com.barlow.app.batch;

import java.util.TimeZone;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class BatchCoreApplication {

	@PostConstruct
	public static void setTimeZone() {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}

	public static void main(String[] args) {
		setTimeZone();
		new SpringApplicationBuilder(BatchCoreApplication.class)
			.web(WebApplicationType.NONE)
			.run(args);
	}
}
