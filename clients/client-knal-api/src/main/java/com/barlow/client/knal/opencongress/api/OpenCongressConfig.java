package com.barlow.client.knal.opencongress.api;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.Retryer;

@EnableFeignClients
@Configuration
public class OpenCongressConfig {

	@Bean(name = "openCongressAuthenticationInterceptor")
	RequestInterceptor interceptor(@Value("${knal.open-congress.api.service-key}") String serviceKey) {
		return template -> template.query("KEY", serviceKey);
	}

	@Bean(name = "openCongressRetryer")
	Retryer retryer() {
		return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
	}

}
