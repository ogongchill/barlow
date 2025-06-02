package com.barlow.core.storage;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationCenterJpaRepository extends JpaRepository<NotificationCenterItemJpaEntity, Long> {

	boolean existsByCreatedAtBetweenAndMemberNo(LocalDateTime startDateTime, LocalDateTime endDateTime, Long memberNo);

	List<NotificationCenterItemJpaEntity> findByMemberNo(Long memberNo);

	@Modifying
	@Query("DELETE FROM NotificationCenterItemJpaEntity nci WHERE nci.memberNo = :memberNo")
	void deleteAllByMemberNo(@Param("memberNo") Long memberNo);
}
