package com.barlow.app.api.controller.v1.reaction;

import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionStatus;
import com.barlow.core.enumerate.ReactionType;

public record ReactionResponse(
	Map<ReactionType, Integer> reactions,
	UserReactionStatus status
) {
	static ReactionResponse from(ReactionStatus reactionStatus) {
		Map<ReactionType, Integer> reactions = reactionStatus.reactions()
			.stream()
			.collect(Collectors.groupingBy(
				Reaction::getReactionType,
				Collectors.collectingAndThen(Collectors.counting(), Long::intValue))
			);
		if (reactionStatus.hasUserReacted()) {
			return new ReactionResponse(
				reactions,
				new UserReactionStatus(reactionStatus.userReaction().getReactionType(), true)
			);
		} else {
			return new ReactionResponse(
				reactions,
				new UserReactionStatus(null, false)
			);
		}
	}

	record UserReactionStatus(
		ReactionType reactionType,
		boolean hasReacted
	) {
	}
}
