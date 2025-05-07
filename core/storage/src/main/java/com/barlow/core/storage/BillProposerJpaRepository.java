package com.barlow.core.storage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillProposerJpaRepository extends JpaRepository<BillProposerJpaEntity, Long> {

	@Query("SELECT bp FROM BillProposerJpaEntity bp WHERE bp.proposeBillId = :billId")
	List<BillProposerJpaEntity> findAllByBillId(@Param("billId") String billId);
}
