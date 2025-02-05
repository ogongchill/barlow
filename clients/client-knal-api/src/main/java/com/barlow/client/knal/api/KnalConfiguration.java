package com.barlow.client.knal.api;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.QueryMapEncoder;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;

@EnableFeignClients
@Configuration
class KnalConfiguration {

	@Bean(name = "authenticationInterceptor")
	RequestInterceptor interceptor(@Value("${knal.api.service-key}") String serviceKey) {
		return template -> template.query("ServiceKey", serviceKey);
	}

	@Bean(name = "knalRetryer")
	Retryer retryer() {
		return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
	}

	@Bean(name = "knalDecoder")
	Decoder decoder() {
		return new KnalDecoder();
	}

	@Bean(name = "knalQueryMapEncoder")
	QueryMapEncoder queryMapEncoder() {
		return new KnalEncoder();
	}
}
