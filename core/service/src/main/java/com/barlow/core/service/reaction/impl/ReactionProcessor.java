package com.barlow.core.service.reaction.impl;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional
	public void react(User user, Reaction reaction) {
		if (reactionRepository.retrieve(user, reaction) != null) {
			throw ReactionDomainException.alreadyReact(reaction.getTargetType().name(), reaction.getTargetId());
		}
		reactionRepository.react(user, reaction);
	}

	@Transactional
	public void removeReaction(User user, Reaction reaction) {
		if (reactionRepository.retrieve(user, reaction) == null) {
			throw ReactionDomainException.alreadyRemoved(reaction.getTargetType().name(), reaction.getTargetId());
		}
		reactionRepository.removeReaction(user, reaction);
	}
}
