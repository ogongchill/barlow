package com.barlow.batch.core.tracebill.job.step;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.tracebill.TraceBillConstant;
import com.barlow.batch.core.tracebill.job.BillPostBatchRepository;
import com.barlow.batch.core.tracebill.job.BillTrackingClient;
import com.barlow.batch.core.tracebill.job.UpdatedBillShareRepository;
import com.barlow.batch.core.tracebill.job.UpdatedBills;
import com.barlow.core.enumerate.LegislationType;

@Component
@StepScope
public class TraceBillUpdateTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final BillTrackingClient client;
	private final UpdatedBillShareRepository billShareRepository;
	private final BillPostBatchRepository billPostBatchRepository;

	public TraceBillUpdateTasklet(
		BillTrackingClient client,
		UpdatedBillShareRepository billShareRepository,
		BillPostBatchRepository billPostBatchRepository
	) {
		this.client = client;
		this.billShareRepository = billShareRepository;
		this.billPostBatchRepository = billPostBatchRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		super.setCurrentExecutionContext(contribution.getStepExecution().getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TraceBillConstant.UPDATED_BILL_SHARE_KEY);
		UpdatedBills updatedBills = billShareRepository.findByKey(hashKey);

		UpdatedBills committeeReceived = updatedBills.filterCommitteeReceived();
		if (!committeeReceived.isEmpty()) {
			committeeReceived.getCommitteeReceived()
				.forEach(billInfo -> {
					LegislationType committee = client.getCommittee(billInfo.billId());
					committeeReceived.assignCommittee(billInfo.billId(), committee);
				});
		}

		billPostBatchRepository.updateAllInBatch(updatedBills);
		return RepeatStatus.FINISHED;
	}
}
