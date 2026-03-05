package com.barlow.core.service.billpost;

import com.barlow.core.domain.Passport;

public interface BillPostViewCountCache {
	boolean shouldCountView(Passport passport, String postId);
}
