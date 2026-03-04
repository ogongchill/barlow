package com.barlow.infra.post.view;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.billpost.BillPostViewCountCache;
import com.github.benmanes.caffeine.cache.Cache;

@Component
public class PostViewCountCacheAdapter implements BillPostViewCountCache {

	private static final String PREFIX = "post-view-count:";

	private final Cache<String, Object> duplicateBlockCache;

	PostViewCountCacheAdapter(Cache<String, Object> duplicateBlockCache) {
		this.duplicateBlockCache = duplicateBlockCache;
	}

	public boolean shouldCountView(Passport passport, String postId) {
		String key = PREFIX + passport.getUserNo() + ":" + postId;
		if (duplicateBlockCache.getIfPresent(key) == null) {
			duplicateBlockCache.put(key, System.currentTimeMillis());
			return true;
		}
		return false;
	}
}
