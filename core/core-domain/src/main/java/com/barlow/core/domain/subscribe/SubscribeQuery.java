package com.barlow.core.domain.subscribe;

import com.barlow.core.domain.User;

public record SubscribeQuery(
	long legislationAccountNo,
	User user
) {
}
