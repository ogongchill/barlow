package com.barlow.core.domain.reaction;

import java.util.List;

public record ReactionStatus(
	List<Reaction> reactions,
	boolean hasReacted
) {
}
