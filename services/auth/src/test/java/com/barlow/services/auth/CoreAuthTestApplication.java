package com.barlow.services.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class CoreAuthTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CoreAuthTestApplication.class, args);
	}
}
