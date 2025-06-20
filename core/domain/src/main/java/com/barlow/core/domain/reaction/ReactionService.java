package com.barlow.core.domain.reaction;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;

@Service
public class ReactionService {

	private final ReactionReader reactionReader;
	private final ReactionProcessor reactionProcessor;

	public ReactionService(ReactionReader reactionReader, ReactionProcessor reactionProcessor) {
		this.reactionReader = reactionReader;
		this.reactionProcessor = reactionProcessor;
	}

	public ReactionStatus retrieveReactions(User user, Reaction reaction) {
		List<Reaction> reactions = reactionReader.retrieveReactions(reaction);
		boolean hasReacted = reactionReader.hasReacted(user, reaction);
		return new ReactionStatus(reactions, hasReacted);
	}

	public long react(User user, Reaction reaction) {
		return reactionProcessor.react(user, reaction);
	}

	public long removeReaction(User user, Reaction reaction) {
		return reactionProcessor.removeReaction(user, reaction);
	}
}
