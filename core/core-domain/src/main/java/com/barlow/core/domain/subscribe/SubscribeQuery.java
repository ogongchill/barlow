package com.barlow.core.domain.subscribe;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

public record SubscribeQuery(
	LegislationType legislationType,
	User user
) {
}
