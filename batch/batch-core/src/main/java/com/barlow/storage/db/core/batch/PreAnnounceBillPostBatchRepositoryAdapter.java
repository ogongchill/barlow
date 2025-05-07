package com.barlow.storage.db.core.batch;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillPostBatchRepository;
import com.barlow.batch.core.preannounce.job.PreviousPreAnnounceBillIds;
import com.barlow.core.storage.BillPostJpaEntity;

@Component
public class PreAnnounceBillPostBatchRepositoryAdapter implements PreAnnounceBillPostBatchRepository {

	private final BillPostBatchJpaRepository billPostBatchJpaRepository;

	public PreAnnounceBillPostBatchRepositoryAdapter(BillPostBatchJpaRepository billPostBatchJpaRepository) {
		this.billPostBatchJpaRepository = billPostBatchJpaRepository;
	}

	@Override
	public PreviousPreAnnounceBillIds retrieveAllInProgress(LocalDate today) {
		return new PreviousPreAnnounceBillIds(
			billPostBatchJpaRepository
				.findAllByPreAnnouncementInfoDeadlineDateGreaterThanEqual(today.atStartOfDay())
				.stream()
				.map(BillPostJpaEntity::getBillId)
				.toList()
		);
	}

	@Override
	public void updateBillPostPreAnnounceInfo(NewPreAnnounceBills newPreAnnounceBills) {
		newPreAnnounceBills.getBillIdWithPreAnnounceBill()
			.forEach((billId, preAnnounceBill) ->
				billPostBatchJpaRepository.updatePreAnnounceInfo(
					billId,
					preAnnounceBill.deadlineDate(),
					preAnnounceBill.linkUrl()
				)
			);
	}
}
