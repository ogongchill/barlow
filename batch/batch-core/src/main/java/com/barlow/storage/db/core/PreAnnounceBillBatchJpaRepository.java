package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface PreAnnounceBillBatchJpaRepository extends JpaRepository<PreAnnounceBillJpaEntity, String> {

	List<PreAnnounceBillJpaEntity> findAllByDeadlineDateGreaterThanEqual(@Param("today") LocalDateTime today);
}
