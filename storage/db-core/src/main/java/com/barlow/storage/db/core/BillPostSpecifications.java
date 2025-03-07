package com.barlow.storage.db.core;

import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public class BillPostSpecifications {

	static Specification<BillPostJpaEntity> isPreAnnouncement(boolean isPreAnnouncementMode) {
		return (root, criteriaQuery, criteriaBuilder)
			-> isPreAnnouncementMode
			? criteriaBuilder.isNotNull(root.get("preAnnouncementInfo"))
			: criteriaBuilder.conjunction();
	}

	static Specification<BillPostJpaEntity> hasLegislationTypeTag(Set<LegislationType> tags) {
		return (root, query, criteriaBuilder)
			-> tags.isEmpty()
			? criteriaBuilder.conjunction()
			: root.get("legislationType").in(tags);
	}

	static Specification<BillPostJpaEntity> hasProgressStatusTag(Set<ProgressStatus> tags) {
		return (root, query, criteriaBuilder)
			-> tags.isEmpty()
			? criteriaBuilder.conjunction()
			: root.get("progressStatus").in(tags);
	}

	static Specification<BillPostJpaEntity> hasProposerTypeTag(Set<ProposerType> tags) {
		return (root, query, criteriaBuilder)
			-> tags.isEmpty()
			? criteriaBuilder.conjunction()
			: root.get("proposerType").in(tags);
	}

	static Specification<BillPostJpaEntity> hasPartyNameTag(Set<PartyName> tags) {
		return (root, query, criteriaBuilder)
			-> tags.isEmpty()
			? criteriaBuilder.conjunction()
			: root.get("partyName").in(tags);
	}

	private BillPostSpecifications() {
	}
}
