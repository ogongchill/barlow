package com.barlow.core.service.billpost;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.barlow.core.domain.billpost.BillPost;
import com.barlow.core.domain.billpost.BillPostDetailQuery;
import com.barlow.core.service.config.Caches;

@Service
public class BillPostCacheService {

	private final BillPostReader billPostReader;
	private final CacheManager cacheManager;

	public BillPostCacheService(BillPostReader billPostReader,
		@Qualifier(Caches.Manager.CAFFEINE) CacheManager cacheManager) {
		this.billPostReader = billPostReader;
		this.cacheManager = cacheManager;
	}

	@Cacheable(cacheNames = Caches.Name.BILL_POST, cacheManager = Caches.Manager.CAFFEINE, key = "#query.billId()")
	public BillPost readBillPost(BillPostDetailQuery query) {
		return billPostReader.readBillPost(query);
	}

	public boolean shouldCountView(long userNo, String billId) {
		Cache cache = cacheManager.getCache(Caches.Name.BILL_POST_VIEW);
		if (cache == null) {
			throw new IllegalStateException(String.format("Cache (%s) not found", Caches.Name.BILL_POST_VIEW));
		}
		String key = userNo + ":" + billId;
		boolean alreadyViewed = cache.get(key) != null;
		if (!alreadyViewed) {
			cache.put(key, true);
		}
		return !alreadyViewed;
	}
}
