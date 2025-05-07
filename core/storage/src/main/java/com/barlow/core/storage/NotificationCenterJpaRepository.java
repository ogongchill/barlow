package com.barlow.core.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationCenterJpaRepository extends JpaRepository<NotificationCenterItemJpaEntity, Long> {

	boolean existsByCreatedAtBetweenAndMemberNo(LocalDateTime startDateTime, LocalDateTime endDateTime, Long memberNo);

	List<NotificationCenterItemJpaEntity> findByMemberNo(Long memberNo);
}
