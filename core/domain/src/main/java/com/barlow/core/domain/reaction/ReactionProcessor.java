package com.barlow.core.domain.reaction;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;

@Component
public class ReactionProcessor {

	private final ReactionRepository reactionRepository;

	public ReactionProcessor(ReactionRepository reactionRepository) {
		this.reactionRepository = reactionRepository;
	}

	public long react(User user, Reaction reaction) {
		if (reactionRepository.retrieve(user, reaction) == null) {
			throw ReactionDomainException.alreadyReact(reaction.getTargetType().name(), reaction.getTargetId());
		}
		return reactionRepository.react(user, reaction);
	}

	@Transactional
	public long removeReaction(User user, Reaction reaction) {
		return reactionRepository.removeReaction(user, reaction);
	}
}
