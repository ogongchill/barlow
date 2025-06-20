package com.barlow.core.storage;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionRepository;

import jakarta.annotation.Nullable;

@Component
public class ReactionRepositoryAdapter implements ReactionRepository {

	private final ReactionJpaRepository reactionJpaRepository;

	public ReactionRepositoryAdapter(ReactionJpaRepository reactionJpaRepository) {
		this.reactionJpaRepository = reactionJpaRepository;
	}

	@Override
	public List<Reaction> retrieve(Reaction reaction) {
		return reactionJpaRepository
			.findAllByTargetIdAndTargetType(reaction.getTargetId(), reaction.getTargetType())
			.stream()
			.map(ReactionJpaEntity::toReaction)
			.toList();
	}

	@Override
	public boolean hasReacted(User user, Reaction reaction) {
		return reactionJpaRepository.findByMemberNoAndTargetIdAndTargetTypeAndType(
			user.getUserNo(),
			reaction.getTargetId(),
			reaction.getTargetType(),
			reaction.getReactionType()
		) != null;
	}

	@Override
	@Nullable
	public Reaction retrieve(User user, Reaction reaction) {
		ReactionJpaEntity reactionJpaEntity = reactionJpaRepository.findByMemberNoAndTargetIdAndTargetTypeAndType(
			user.getUserNo(),
			reaction.getTargetId(),
			reaction.getTargetType(),
			reaction.getReactionType()
		);
		if (reactionJpaEntity == null) {
			return null;
		}
		return reactionJpaEntity.toReaction();
	}

	@Override
	public long react(User user, Reaction reaction) {
		return reactionJpaRepository.save(
			new ReactionJpaEntity(
				user.getUserNo(),
				reaction.getTargetId(),
				reaction.getTargetType(),
				reaction.getReactionType()
			)
		).getNo();
	}

	@Override
	public long removeReaction(User user, Reaction reaction) {
		ReactionJpaEntity reactionJpaEntity = reactionJpaRepository.findByMemberNoAndTargetIdAndTargetTypeAndType(
			user.getUserNo(),
			reaction.getTargetId(),
			reaction.getTargetType(),
			reaction.getReactionType()
		);
		if (reactionJpaEntity == null) {
			return -1;
		}
		reactionJpaRepository.delete(reactionJpaEntity);
		return reactionJpaEntity.getNo();
	}
}
