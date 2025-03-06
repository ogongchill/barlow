package com.barlow.client.knal.opendata.api;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import feign.QueryMapEncoder;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.Decoder;

class OpenDataConfig {

	@Bean(name = "authenticationInterceptor")
	RequestInterceptor interceptor(@Value("${knal.open-data.api.service-key}") String serviceKey) {
		return template -> template.query("ServiceKey", serviceKey);
	}

	@Bean(name = "openDataRetryer")
	Retryer retryer() {
		return new Retryer.Default(100L, TimeUnit.SECONDS.toMillis(1L), 3);
	}

	@Bean(name = "openDataDecoder")
	Decoder decoder() {
		return new OpenDataResponseDecoder();
	}

	@Bean(name = "openDataQueryMapEncoder")
	QueryMapEncoder queryMapEncoder() {
		return new OpenDataRequestEncoder();
	}
}
