package com.barlow.batch.admin.bill.job.step;

import java.util.stream.Collectors;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import com.barlow.infra.storage.batch.BillPostBatchJpaRepository;
import com.barlow.infra.storage.batch.LegislationAccountBatchJpaRepository;

@Component
@StepScope
public class BillLegislationBodyWriter implements ItemWriter<BillLegislationBody> {

	private final BillPostBatchJpaRepository billPostBatchJpaRepository;
	private final LegislationAccountBatchJpaRepository legislationAccountBatchJpaRepository;

	public BillLegislationBodyWriter(
		BillPostBatchJpaRepository billPostBatchJpaRepository,
		LegislationAccountBatchJpaRepository legislationAccountBatchJpaRepository
	) {
		this.billPostBatchJpaRepository = billPostBatchJpaRepository;
		this.legislationAccountBatchJpaRepository = legislationAccountBatchJpaRepository;
	}

	@Override
	public void write(Chunk<? extends BillLegislationBody> chunk) {
		chunk.getItems()
			.stream()
			.collect(Collectors.groupingBy(
				BillLegislationBody::committee,
				Collectors.mapping(BillLegislationBody::billId, Collectors.toList())
			))
			.forEach((legislationType, billIds) -> {
					billPostBatchJpaRepository.updateLegislationTypeInBatchByAdmin(legislationType, billIds);
					legislationAccountBatchJpaRepository.updateAccountPostCount(
						billIds.size(), legislationType.getLegislationNo());
				}
			);
	}
}
