package com.barlow.core.domain.reaction;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface ReactionRepository {

	List<Reaction> retrieve(ReactionQuery reactionQuery);

	Reaction retrieveUserReaction(User user, ReactionQuery reactionQuery);

	Reaction retrieve(User user, Reaction reaction);

	void react(User user, Reaction reaction);

	void removeReaction(User user, Reaction reaction);
}
