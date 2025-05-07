package com.barlow.app.batch.tracebill.job.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.tracebill.TraceBillConstant;
import com.barlow.app.batch.tracebill.job.BillPostBatchRepository;
import com.barlow.app.batch.tracebill.job.BillTrackingClient;
import com.barlow.app.batch.tracebill.job.LegislationAccountBatchRepository;
import com.barlow.app.batch.tracebill.job.UpdatedBillShareRepository;
import com.barlow.app.batch.tracebill.job.UpdatedBills;
import com.barlow.core.enumerate.LegislationType;

@Component
@StepScope
public class TraceBillUpdateTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final BillTrackingClient client;
	private final UpdatedBillShareRepository billShareRepository;
	private final BillPostBatchRepository billPostBatchRepository;
	private final LegislationAccountBatchRepository accountBatchRepository;

	public TraceBillUpdateTasklet(
		BillTrackingClient client,
		UpdatedBillShareRepository billShareRepository,
		BillPostBatchRepository billPostBatchRepository,
		LegislationAccountBatchRepository accountBatchRepository
	) {
		this.client = client;
		this.billShareRepository = billShareRepository;
		this.billPostBatchRepository = billPostBatchRepository;
		this.accountBatchRepository = accountBatchRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TraceBillConstant.UPDATED_BILL_SHARE_KEY);
		UpdatedBills updatedBills = billShareRepository.findByKey(hashKey);

		if (updatedBills.isEmpty()) {
			return RepeatStatus.FINISHED;
		}

		UpdatedBills committeeReceived = updatedBills.filterCommitteeReceived();
		if (!committeeReceived.isEmpty()) {
			updatedBills.getCommitteeReceived()
				.forEach(billInfo -> {
					LegislationType committee = client.getCommittee(billInfo.billId());
					updatedBills.assignCommittee(billInfo.billId(), committee);
				});
			accountBatchRepository.updateAccountBillCount(updatedBills);
		}

		billPostBatchRepository.updateAllInBatch(updatedBills);
		return RepeatStatus.FINISHED;
	}
}
