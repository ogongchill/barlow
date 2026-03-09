package com.barlow.core.service.reaction;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionQuery;
import com.barlow.core.domain.reaction.ReactionStatus;
import com.barlow.core.service.reaction.impl.ReactionProcessor;
import com.barlow.core.service.reaction.impl.ReactionReader;

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

	@Transactional
	public void react(User user, Reaction reaction) {
		reactionProcessor.react(user, reaction);
	}

	@Transactional
	public void removeReaction(User user, Reaction reaction) {
		reactionProcessor.removeReaction(user, reaction);
	}
}
