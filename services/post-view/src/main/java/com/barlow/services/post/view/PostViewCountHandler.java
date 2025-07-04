package com.barlow.services.post.view;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.Passport;
import com.github.benmanes.caffeine.cache.Cache;

@Component
public class PostViewCountHandler {

	private final Cache<String, Object> duplicateBlockCache;
	private final PostViewCountIncreaser postViewCountIncreaser;

	PostViewCountHandler(Cache<String, Object> duplicateBlockCache, PostViewCountIncreaser postViewCountIncreaser) {
		this.duplicateBlockCache = duplicateBlockCache;
		this.postViewCountIncreaser = postViewCountIncreaser;
	}

	public void handleViewCount(Passport passport, String postId) {
		String key = passport.getUserNo() + ":" + postId;
		if (duplicateBlockCache.getIfPresent(key) == null) {
			duplicateBlockCache.put(key, System.currentTimeMillis());
			postViewCountIncreaser.increaseCount(postId);
		}
	}
}
