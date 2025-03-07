package com.barlow.storage.db.core;

import org.springframework.stereotype.Component;

import com.barlow.batch.core.preannounce.job.NewPreAnnounceBills;
import com.barlow.batch.core.preannounce.job.PreAnnounceBillPostBatchRepository;

@Component
public class PreAnnounceBillPostBatchRepositoryAdapter implements PreAnnounceBillPostBatchRepository {

	private final BillPostBatchJpaRepository billPostBatchJpaRepository;

	public PreAnnounceBillPostBatchRepositoryAdapter(BillPostBatchJpaRepository billPostBatchJpaRepository) {
		this.billPostBatchJpaRepository = billPostBatchJpaRepository;
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
