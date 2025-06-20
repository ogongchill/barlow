package com.barlow.app.api.controller.v1.reaction;

import java.util.Map;
import java.util.stream.Collectors;

import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionStatus;

public record ReactionResponse(
	Map<String, Integer> reactions,
	boolean hasReacted
) {
	static ReactionResponse from(ReactionStatus reactionStatus) {
		Map<String, Integer> reactions = reactionStatus.reactions()
			.stream()
			.collect(Collectors.groupingBy(
				Reaction::getReactionTypeName,
				Collectors.collectingAndThen(Collectors.counting(), Long::intValue))
			);
		return new ReactionResponse(
			reactions,
			reactionStatus.hasReacted()
		);
	}
}
