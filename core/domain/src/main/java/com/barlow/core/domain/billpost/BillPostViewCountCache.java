package com.barlow.core.domain.billpost;

import com.barlow.core.domain.Passport;

public interface BillPostViewCountCache {
	boolean shouldCountView(Passport passport, String postId);
}
