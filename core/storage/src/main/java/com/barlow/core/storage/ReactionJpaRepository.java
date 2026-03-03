package com.barlow.core.storage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

public interface ReactionJpaRepository extends JpaRepository<ReactionJpaEntity, Long> {

	List<ReactionJpaEntity> findAllByTargetIdAndTargetType(String targetId, ReactionTarget targetType);

	ReactionJpaEntity findByMemberNoAndTargetIdAndTargetType(Long memberNo, String targetId, ReactionTarget targetType);

	ReactionJpaEntity findByMemberNoAndTargetIdAndTargetTypeAndType(
		Long memberNo,
		String targetId,
		ReactionTarget targetType,
		ReactionType type
	);

	@Modifying
	@Query("""
		DELETE FROM ReactionJpaEntity r WHERE r.memberNo = :memberNo
		AND r.targetId = :targetId
		AND r.targetType = :targetType
		AND r.type = :reactionType""")
	void deleteByMemberNoTargetIdAndTargetTypeAndReactionType(
		@Param("memberNo") Long memberNo,
		@Param("targetId") String targetId,
		@Param("targetType") ReactionTarget targetType,
		@Param("reactionType") ReactionType reactionType
	);
}
