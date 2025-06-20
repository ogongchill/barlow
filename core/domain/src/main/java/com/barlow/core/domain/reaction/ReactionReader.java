package com.barlow.core.domain.reaction;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class ReactionReader {

	private final ReactionRepository reactionRepository;

	ReactionReader(ReactionRepository reactionRepository) {
		this.reactionRepository = reactionRepository;
	}

	public List<Reaction> retrieveReactions(Reaction reaction) {
		return reactionRepository.retrieve(reaction);
	}

	public boolean hasReacted(User user, Reaction reaction) {
		return reactionRepository.hasReacted(user, reaction);
	}
}
