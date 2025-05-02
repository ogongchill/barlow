package com.barlow.storage.db.core;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.data.jpa.domain.Specification;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

public class BillPostSpecifications {

	static Specification<BillPostJpaEntity> isPreAnnouncement(boolean isPreAnnouncementMode) {
		return (root, criteriaQuery, criteriaBuilder)
			-> isPreAnnouncementMode
			? criteriaBuilder.greaterThanOrEqualTo(root.get("preAnnouncementInfo").get("deadlineDate"), LocalDate.now().atStartOfDay())
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
		return (root, query, criteriaBuilder) -> {
			if (tags.isEmpty()) {
				return criteriaBuilder.conjunction();
			}
			Subquery<String> subquery = query.subquery(String.class);
			Root<BillProposerJpaEntity> billProposerRoot = subquery.from(BillProposerJpaEntity.class);
			subquery.select(billProposerRoot.get("proposeBillId"))
				.where(
					criteriaBuilder.equal(root.get("billId"), billProposerRoot.get("proposeBillId")),
					billProposerRoot.get("partyName").in(tags)
				);
			return criteriaBuilder.exists(subquery);
		};
	}

	private BillPostSpecifications() {
	}
}
