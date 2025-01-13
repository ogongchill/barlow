package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationCenterJpaRepository extends JpaRepository<NotificationCenterJpaEntity, Long> {

	boolean existsByCreatedAtBetweenAndMemberNo(LocalDateTime startDateTime, LocalDateTime endDateTime, Long memberNo);

	List<NotificationCenterJpaEntity> findByMemberNo(Long memberNo);
}
