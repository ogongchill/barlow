package com.barlow.batch.core.recentbill.job.listener;

import static com.barlow.batch.core.recentbill.RecentBillConstant.BATCH_DATE_JOB_PARAMETER;
import static com.barlow.batch.core.recentbill.RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.TodayBillRetrieveClient;
import com.barlow.batch.core.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.batch.core.utils.HashUtil;
import com.barlow.client.knal.opendata.api.OpenDataException;

@Component
public class RetrieveTodayBillJobListener
	extends AbstractExecutionContextSharingManager
	implements JobExecutionListener {

	private final TodayBillRetrieveClient client;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public RetrieveTodayBillJobListener(
		TodayBillRetrieveClient client,
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.client = client;
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public void beforeJob(@NotNull JobExecution jobExecution) throws OpenDataException {
		LocalDate batchDate = jobExecution.getJobParameters().getLocalDate(BATCH_DATE_JOB_PARAMETER);
		TodayBillInfoBatchEntity todayBillInfo = client.getTodayBillInfo(batchDate);

		String hashKey = HashUtil.generate(todayBillInfo);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(TODAY_BILL_INFO_SHARE_KEY, hashKey);

		jobScopeShareRepository.save(hashKey, todayBillInfo);
	}
}
