package com.barlow.batch.core.tracebill.job.step;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.tracebill.TraceBillConstant;
import com.barlow.batch.core.tracebill.job.BillTrackingClient;
import com.barlow.batch.core.tracebill.job.PreviousBillBatchRepository;
import com.barlow.batch.core.tracebill.job.CurrentBillInfoResult;
import com.barlow.batch.core.tracebill.job.PreviousBills;
import com.barlow.batch.core.tracebill.job.UpdatedBillShareRepository;
import com.barlow.batch.core.tracebill.job.UpdatedBills;
import com.barlow.batch.core.utils.HashUtil;

@Component
@StepScope
public class TraceBillDirtyCheckTasklet extends AbstractExecutionContextSharingManager implements Tasklet {

	private final BillTrackingClient client;
	private final PreviousBillBatchRepository previousBillBatchRepository;
	private final UpdatedBillShareRepository billShareRepository;

	public TraceBillDirtyCheckTasklet(
		BillTrackingClient client,
		PreviousBillBatchRepository previousBillBatchRepository,
		UpdatedBillShareRepository billShareRepository
	) {
		this.client = client;
		this.previousBillBatchRepository = previousBillBatchRepository;
		this.billShareRepository = billShareRepository;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
		JobExecution jobExecution = contribution.getStepExecution().getJobExecution();
		JobParameters jobParameters = jobExecution.getJobParameters();
		LocalDate startDate = jobParameters.getLocalDate(TraceBillConstant.TRACKING_START_DATE_JOB_PARAMETER);
		LocalDate endDate = jobParameters.getLocalDate(TraceBillConstant.TRACKING_END_DATE_JOB_PARAMETER);

		CurrentBillInfoResult currentBillInfo = client.getTraceBillInfo(startDate, endDate);
		PreviousBills previousBills = new PreviousBills(
			previousBillBatchRepository.findAllPreviousBetween(startDate, endDate)
		);
		UpdatedBills updatedBills = previousBills.dirtyCheck(currentBillInfo);

		String hashKey = HashUtil.generate(updatedBills);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(TraceBillConstant.UPDATED_BILL_SHARE_KEY, hashKey);
		billShareRepository.save(hashKey, updatedBills);

		return RepeatStatus.FINISHED;
	}
}
