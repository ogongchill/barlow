package com.barlow.services.post.view;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.Passport;
import com.github.benmanes.caffeine.cache.Cache;

@Component
public class PostViewCountHandler {

	private static final String PREFIX = "post-view-count:";

	private final Cache<String, Object> duplicateBlockCache;

	PostViewCountHandler(Cache<String, Object> duplicateBlockCache) {
		this.duplicateBlockCache = duplicateBlockCache;
	}

	public boolean checkAndUpdate(Passport passport, String postId) {
		String key = PREFIX + passport.getUserNo() + ":" + postId;
		if (duplicateBlockCache.getIfPresent(key) == null) {
			duplicateBlockCache.put(key, System.currentTimeMillis());
			return true;
		}
		return false;
	}
}
