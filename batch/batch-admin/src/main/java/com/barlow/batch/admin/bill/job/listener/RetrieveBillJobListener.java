package com.barlow.batch.admin.bill.job.listener;

import static com.barlow.batch.admin.bill.BillConstant.BILL_INFO_SHARE_KEY;
import static com.barlow.batch.admin.bill.BillConstant.END_DATE_JOB_PARAMETER;
import static com.barlow.batch.admin.bill.BillConstant.START_DATE_JOB_PARAMETER;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.utils.HashUtil;
import com.barlow.batch.admin.common.AdminAbstractExecutionContextSharingManager;
import com.barlow.batch.admin.bill.job.BillJobScopeShareRepository;
import com.barlow.batch.admin.bill.job.BillInfoBatchEntity;
import com.barlow.batch.admin.bill.job.BillRetrieveClient;

@Component
public class RetrieveBillJobListener
	extends AdminAbstractExecutionContextSharingManager
	implements JobExecutionListener {

	private final BillRetrieveClient client;
	private final BillJobScopeShareRepository jobScopeShareRepository;

	public RetrieveBillJobListener(
		BillRetrieveClient client,
		BillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.client = client;
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public void beforeJob(@NotNull JobExecution jobExecution) {
		LocalDate startDate = jobExecution.getJobParameters().getLocalDate(START_DATE_JOB_PARAMETER);
		LocalDate endDate = jobExecution.getJobParameters().getLocalDate(END_DATE_JOB_PARAMETER);
		BillInfoBatchEntity billInfo = client.getAllBillInfo(startDate, endDate);

		String hashKey = HashUtil.generate(billInfo);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(BILL_INFO_SHARE_KEY, hashKey);

		jobScopeShareRepository.save(hashKey, billInfo);
	}
}
