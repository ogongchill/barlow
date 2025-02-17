package com.barlow.storage.db.core;

import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillPostJpaRepository extends JpaRepository<BillPostJpaEntity, Long> {

	@Query("""
		SELECT rbp FROM BillPostJpaEntity rbp
		WHERE rbp.legislationType IN :legislationTypes
		AND rbp.progressStatus IN :progressStatuses
		AND rbp.proposerType IN :proposerTypes""")
	Slice<BillPostJpaEntity> findAllBy(
		Set<LegislationType> legislationTypes,
		Set<ProgressStatus> progressStatuses,
		Set<ProposerType> proposerTypes,
		Pageable pageable
	);

	@Query("""
		SELECT rbp FROM BillPostJpaEntity rbp
		INNER JOIN BillProposerJpaEntity bp ON rbp.billId = bp.proposeBillId
		WHERE rbp.legislationType IN :legislationTypes
		AND rbp.progressStatus IN :progressStatuses
		AND rbp.proposerType IN :proposerTypes
		AND bp.partyName IN :parties""")
	Slice<BillPostJpaEntity> findAllBy(
		Set<LegislationType> legislationTypes,
		Set<ProgressStatus> progressStatuses,
		Set<ProposerType> proposerTypes,
		Set<PartyName> parties,
		Pageable pageable
	);

	BillPostJpaEntity findByBillId(String billId);
}
