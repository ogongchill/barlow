package com.barlow.core.domain.reaction;

import com.barlow.core.enumerate.ReactionTarget;

public record ReactionQuery(
	String targetId,
	ReactionTarget targetType
) {
}
