package com.barlow.core.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillPostJpaRepository
	extends JpaRepository<BillPostJpaEntity, Long>,
	JpaSpecificationExecutor<BillPostJpaEntity> {

	BillPostJpaEntity findByBillId(String billId);

	List<BillPostJpaEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

	@Modifying
	@Query("UPDATE BillPostJpaEntity bp SET bp.viewCount = bp.viewCount + 1 WHERE bp.billId = :billId")
	void updateViewCount(@Param("billId") String billId);
}
