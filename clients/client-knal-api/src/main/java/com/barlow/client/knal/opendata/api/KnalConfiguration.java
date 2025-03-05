package com.barlow.client.knal.opendata.api;

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
	RequestInterceptor interceptor(@Value("${knal.open-data.api.service-key}") String serviceKey) {
		return template -> template.query("ServiceKey", serviceKey);
	}

	@Bean(name = "knalOpenDataRetryer")
	Retryer retryer() {
		return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
	}

	@Bean(name = "knalOpenDataDecoder")
	Decoder decoder() {
		return new KnalDecoder();
	}

	@Bean(name = "knalOpenDataQueryMapEncoder")
	QueryMapEncoder queryMapEncoder() {
		return new KnalEncoder();
	}
}
