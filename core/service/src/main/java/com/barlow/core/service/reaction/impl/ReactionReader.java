package com.barlow.core.service.reaction.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionQuery;
import com.barlow.core.domain.reaction.ReactionRepository;

@Component
public class ReactionReader {

	private final ReactionRepository reactionRepository;

	ReactionReader(ReactionRepository reactionRepository) {
		this.reactionRepository = reactionRepository;
	}

	public List<Reaction> readReactions(ReactionQuery reactionQuery) {
		return reactionRepository.retrieve(reactionQuery);
	}

	public Reaction readUserReaction(User user, ReactionQuery reactionQuery) {
		return reactionRepository.retrieveUserReaction(user, reactionQuery);
	}
}
