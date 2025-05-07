package com.barlow.services.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class ServiceAuthTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAuthTestApplication.class, args);
	}
}
