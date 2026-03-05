package com.barlow.core.service.config;

import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableCaching
@Configuration
public class CacheConfig {

	@Bean(name = Caches.Manager.CAFFEINE)
	public CacheManager caffeinCacheManager() {
		CaffeineCacheManager cacheManager = new CaffeineCacheManager();

		cacheManager.registerCustomCache(Caches.Name.BILL_POST,
			Caffeine.newBuilder()
				.maximumSize(500)
				.expireAfterWrite(30, TimeUnit.MINUTES)
				.recordStats()
				.build()
		);
		cacheManager.registerCustomCache(Caches.Name.BILL_POST_VIEW,
			Caffeine.newBuilder()
				.maximumSize(10_000)
				.expireAfterWrite(1, TimeUnit.HOURS)
				.recordStats()
				.build()
		);
		return cacheManager;
	}
}
