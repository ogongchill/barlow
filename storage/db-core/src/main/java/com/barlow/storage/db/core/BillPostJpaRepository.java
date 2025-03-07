package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillPostJpaRepository
	extends JpaRepository<BillPostJpaEntity, Long>,
	JpaSpecificationExecutor<BillPostJpaEntity> {

	BillPostJpaEntity findByBillId(String billId);

	List<BillPostJpaEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
