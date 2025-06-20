package com.barlow.core.domain.reaction;

import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

public class Reaction {

	private final String targetId;
	private final ReactionTarget targetType;
	private final ReactionType reactionType;

	public Reaction(String targetId, ReactionTarget targetType, ReactionType reactionType) {
		this.targetId = targetId;
		this.targetType = targetType;
		this.reactionType = reactionType;
	}

	public String getTargetId() {
		return targetId;
	}

	public ReactionTarget getTargetType() {
		return targetType;
	}

	public ReactionType getReactionType() {
		return reactionType;
	}

	public String getReactionTypeName() {
		return reactionType.name();
	}
}
