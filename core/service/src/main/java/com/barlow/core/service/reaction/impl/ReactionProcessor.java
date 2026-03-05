package com.barlow.core.service.reaction.impl;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionDomainException;
import com.barlow.core.domain.reaction.ReactionRepository;

@Component
public class ReactionProcessor {

	private final ReactionRepository reactionRepository;

	public ReactionProcessor(ReactionRepository reactionRepository) {
		this.reactionRepository = reactionRepository;
	}

	public void react(User user, Reaction reaction) {
		if (reactionRepository.retrieve(user, reaction) != null) {
			throw ReactionDomainException.alreadyReact(reaction.getTargetType().name(), reaction.getTargetId());
		}
		reactionRepository.react(user, reaction);
	}

	public void removeReaction(User user, Reaction reaction) {
		if (reactionRepository.retrieve(user, reaction) == null) {
			throw ReactionDomainException.alreadyRemoved(reaction.getTargetType().name(), reaction.getTargetId());
		}
		reactionRepository.removeReaction(user, reaction);
	}
}
