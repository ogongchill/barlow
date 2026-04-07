package com.barlow.infra.storage.batch;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.infra.storage.NotificationCenterItemJpaEntity;

public interface NotificationCenterItemBatchJpaRepository
	extends JpaRepository<NotificationCenterItemJpaEntity, Long> {

	@Query("SELECT e.no FROM NotificationCenterItemJpaEntity e WHERE e.createdAt < :threshold")
	List<Long> findIdsByCreatedAtBefore(@Param("threshold") LocalDateTime threshold, Pageable pageable);

	@Query("SELECT COUNT(e) FROM NotificationCenterItemJpaEntity e WHERE e.createdAt < :threshold")
	long countByCreatedAtBefore(@Param("threshold") LocalDateTime threshold);
}
