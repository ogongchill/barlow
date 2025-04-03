package com.barlow.storage.db.core;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.ProgressStatus;

public interface BillPostBatchJpaRepository extends JpaRepository<BillPostJpaEntity, Long> {

	@Modifying
	@Query("""
		UPDATE BillPostJpaEntity bp
		SET bp.progressStatus = 'COMMITTEE_RECEIVED', bp.legislationType = :legislationType
		WHERE bp.billId IN :billIds""")
	void updateLegislationTypeInBatch(
		@Param("legislationType") LegislationType legislationType,
		@Param("billIds") List<String> billIds
	);

	@Modifying
	@Query("UPDATE BillPostJpaEntity bp SET bp.progressStatus = :progressStatus WHERE bp.billId IN :billIds")
	void updateProgressStatusInBatch(
		@Param("progressStatus") ProgressStatus progressStatus,
		@Param("billIds") List<String> billIds
	);

	@Modifying
	@Query("""
		UPDATE BillPostJpaEntity bp
		SET bp.preAnnouncementInfo.deadlineDate = :deadlineDate, bp.preAnnouncementInfo.linkUrl = :linkUrl
		WHERE bp.billId = :billId""")
	void updatePreAnnounceInfo(
		@Param("billId") String billId,
		@Param("deadlineDate") LocalDateTime deadlineDate,
		@Param("linkUrl") String linkUrl
	);

	List<BillPostJpaEntity> findAllByPreAnnouncementInfoDeadlineDateGreaterThanEqual(LocalDateTime deadlineDate);

	List<BillPostJpaEntity> findAllByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}
