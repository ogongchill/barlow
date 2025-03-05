package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.PartyName;
import com.barlow.core.enumerate.ProgressStatus;
import com.barlow.core.enumerate.ProposerType;

public interface BillPostJpaRepository extends JpaRepository<BillPostJpaEntity, Long> {

	@Query("""
		SELECT rbp FROM BillPostJpaEntity rbp
		WHERE rbp.legislationType IN :legislationTypes
		AND rbp.progressStatus IN :progressStatuses
		AND rbp.proposerType IN :proposerTypes""")
	Slice<BillPostJpaEntity> findAllBy(
		@Param("legislationTypes") Set<LegislationType> legislationTypes,
		@Param("progressStatuses") Set<ProgressStatus> progressStatuses,
		@Param("proposerTypes") Set<ProposerType> proposerTypes,
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
		@Param("legislationTypes") Set<LegislationType> legislationTypes,
		@Param("progressStatuses") Set<ProgressStatus> progressStatuses,
		@Param("proposerTypes") Set<ProposerType> proposerTypes,
		@Param("parties") Set<PartyName> parties,
		Pageable pageable
	);

	BillPostJpaEntity findByBillId(String billId);

	List<BillPostJpaEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
