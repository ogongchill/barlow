package com.barlow.core.domain.reaction;

import java.util.List;

public record ReactionStatus(
	List<Reaction> reactions,
	Reaction userReaction
) {
	public boolean hasUserReacted() {
		return userReaction != null;
	}
}
