package com.barlow.core.service.config;

public interface Caches {

	interface Manager {
		String CAFFEINE = "caffeineCacheManager";
		String REDIS = "redisCacheManager";
	}

	interface Name {
		String BILL_POST = "billPost";
		String BILL_POST_VIEW = "billPostView";
	}
}
