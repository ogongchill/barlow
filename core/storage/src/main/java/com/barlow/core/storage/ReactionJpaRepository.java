package com.barlow.core.storage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

public interface ReactionJpaRepository extends JpaRepository<ReactionJpaEntity, Long> {

	List<ReactionJpaEntity> findAllByTargetIdAndTargetType(String targetId, ReactionTarget targetType);

	ReactionJpaEntity findByMemberNoAndTargetIdAndTargetTypeAndType(Long memberNo, String targetId, ReactionTarget targetType, ReactionType type);

	ReactionJpaEntity findByMemberNoAndTargetIdAndTargetType(Long memberNo, String targetId, ReactionTarget targetType);
}
