package com.barlow.infra.post.view.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
public class CacheConfiguration {

	@Bean
	public Cache<String, Object> duplicateBlockCache() {
		return Caffeine.newBuilder().expireAfterWrite(30, TimeUnit.MINUTES).maximumSize(3000).build();
	}
}
