package com.barlow.storage.db.core;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreAnnounceBillBatchJpaRepository extends JpaRepository<PreAnnounceBillJpaEntity, String> {

	@Query("SELECT pab FROM PreAnnounceBillJpaEntity pab WHERE pab.progressStatus = :inProgressStatus")
	List<PreAnnounceBillJpaEntity> findAllInProgress(@Param("inProgressStatus") String inProgressStatus);
}
