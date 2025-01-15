package com.barlow.storage.db.core;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RecentBillPostJpaRepository extends JpaRepository<RecentBillPostJpaEntity, Long> {

	@Query("""
		SELECT rbp FROM RecentBillPostJpaEntity rbp
		WHERE rbp.legislationType IN :legislationTypes
		AND rbp.legislationStatus IN :legislationStatuses
		AND rbp.proposerType IN :proposerTypes""")
	Slice<RecentBillPostJpaEntity> findAllBy(
		Set<LegislationType> legislationTypes,
		Set<LegislationStatus> legislationStatuses,
		Set<ProposerType> proposerTypes,
		Pageable pageable
	);

	@Query("""
		SELECT rbp FROM RecentBillPostJpaEntity rbp
		INNER JOIN BillProposerJpaEntity bp ON rbp.billId = bp.proposeBillId
		WHERE rbp.legislationType IN :legislationTypes
		AND rbp.legislationStatus IN :legislationStatuses
		AND rbp.proposerType IN :proposerTypes
		AND bp.partyName IN :parties""")
	Slice<RecentBillPostJpaEntity> findAllBy(
		Set<LegislationType> legislationTypes,
		Set<LegislationStatus> legislationStatuses,
		Set<ProposerType> proposerTypes,
		Set<PartyName> parties,
		Pageable pageable
	);

	RecentBillPostJpaEntity findByBillId(String billId);
}
