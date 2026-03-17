package com.barlow.infra.storage.batch;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.infra.storage.NotificationCenterItemJpaEntity;

public interface NotificationCenterItemBatchJpaRepository
	extends JpaRepository<NotificationCenterItemJpaEntity, Long> {

	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM NotificationCenterItemJpaEntity nci WHERE nci.createdAt < :threshold")
	int deleteByCreatedAtBefore(@Param("threshold") LocalDateTime threshold);
}
