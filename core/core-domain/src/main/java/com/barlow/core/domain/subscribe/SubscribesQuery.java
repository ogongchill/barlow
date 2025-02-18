package com.barlow.core.domain.subscribe;

import java.util.List;

import com.barlow.core.domain.User;

public record SubscribesQuery(
	List<String> legislationTypes,
	User user
) {
}
