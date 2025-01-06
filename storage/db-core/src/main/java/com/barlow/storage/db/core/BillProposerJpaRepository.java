package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.domain.recentbill.BillProposer;

public interface BillProposerJpaRepository extends JpaRepository<BillProposer, Long> {

	@Query("SELECT bp FROM BillProposerJpaEntity bp WHERE bp.proposeBillId = :billId")
	List<BillProposerJpaEntity> findAllByBillId(@Param("billId") String billId);
}
