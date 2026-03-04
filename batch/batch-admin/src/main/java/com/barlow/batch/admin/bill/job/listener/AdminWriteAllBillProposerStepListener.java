package com.barlow.batch.admin.bill.job.listener;

import static com.barlow.batch.admin.bill.BillConstant.BILL_INFO_SHARE_KEY;
import static com.barlow.batch.admin.bill.BillConstant.BILL_PROPOSER_READER_INDEX_KEY;
import static com.barlow.batch.admin.bill.BillConstant.BILL_WITH_FEW_PROPOSERS_SHARE_KEY;
import static com.barlow.batch.admin.bill.BillConstant.END_DATE_JOB_PARAMETER;
import static com.barlow.batch.admin.bill.BillConstant.START_DATE_JOB_PARAMETER;

import java.time.LocalDate;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.utils.HashUtil;
import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillJobScopeShareRepository;
import com.barlow.batch.admin.bill.job.BillRetrieveClient;
import com.barlow.batch.admin.common.AdminAbstractExecutionContextSharingManager;

@Component
@StepScope
public class AdminWriteAllBillProposerStepListener
	extends AdminAbstractExecutionContextSharingManager
	implements StepExecutionListener {

	private final BillJobScopeShareRepository jobScopeShareRepository;
	private final BillRetrieveClient client;

	public AdminWriteAllBillProposerStepListener(
		BillJobScopeShareRepository jobScopeShareRepository,
		BillRetrieveClient client
	) {
		super();
		this.client = client;
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		JobExecution jobExecution = stepExecution.getJobExecution();
		BillInfoBatchEntity billInfo;
		if (stepExecution.getExecutionContext().get(BILL_PROPOSER_READER_INDEX_KEY) != null) {
			LocalDate startDate = jobExecution.getJobParameters().getLocalDate(START_DATE_JOB_PARAMETER);
			LocalDate endDate = jobExecution.getJobParameters().getLocalDate(END_DATE_JOB_PARAMETER);
			billInfo = client.getAllBillInfo(startDate, endDate);
		} else {
			super.setCurrentExecutionContext(jobExecution.getExecutionContext());
			String hashKey = super.getDataFromJobExecutionContext(BILL_INFO_SHARE_KEY);
			billInfo = jobScopeShareRepository.findByKey(hashKey);
		}

		BillInfoBatchEntity billsWithFewProposers = billInfo.filteredBillsWithFewProposers();
		String newHashKey = HashUtil.generate(billsWithFewProposers);

		super.setCurrentExecutionContext(stepExecution.getExecutionContext());
		super.putDataToExecutionContext(BILL_WITH_FEW_PROPOSERS_SHARE_KEY, newHashKey);

		jobScopeShareRepository.save(newHashKey, billsWithFewProposers);
	}
}

