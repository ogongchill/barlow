package com.barlow.storage.db.core.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.storage.LegislationAccountJpaEntity;

public interface LegislationAccountBatchJpaRepository extends JpaRepository<LegislationAccountJpaEntity, Long> {

	@Modifying
	@Query("UPDATE LegislationAccountJpaEntity la SET la.postCount = la.postCount + :postCount WHERE la.no = :accountNo")
	void updateAccountPostCount(@Param("postCount") int postCount, @Param("accountNo") long accountNo);
}
