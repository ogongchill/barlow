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

	public ReactionStatus retrieveReactions(User user, ReactionQuery reactionQuery) {
		List<Reaction> reactions = reactionReader.readReactions(reactionQuery);
		Reaction userReaction = reactionReader.readUserReaction(user, reactionQuery);
		return new ReactionStatus(reactions, userReaction);
	}

	public void react(User user, Reaction reaction) {
		reactionProcessor.react(user, reaction);
	}

	public void removeReaction(User user, Reaction reaction) {
		reactionProcessor.removeReaction(user, reaction);
	}
}
