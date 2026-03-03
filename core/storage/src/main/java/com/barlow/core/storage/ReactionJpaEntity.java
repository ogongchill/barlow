package com.barlow.core.storage;

import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reaction")
public class ReactionJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long no;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	@Column(name = "target_id", nullable = false)
	private String targetId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(20)", name = "target_type", nullable = false)
	private ReactionTarget targetType;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(20)", name = "reaction_type", nullable = false)
	private ReactionType type;

	protected ReactionJpaEntity() {
	}

	public ReactionJpaEntity(Long memberNo, String targetId, ReactionTarget targetType, ReactionType type) {
		this.memberNo = memberNo;
		this.targetId = targetId;
		this.targetType = targetType;
		this.type = type;
	}

	Reaction toReaction() {
		return new Reaction(targetId, targetType, type);
	}

	Long getNo() {
		return no;
	}
}
