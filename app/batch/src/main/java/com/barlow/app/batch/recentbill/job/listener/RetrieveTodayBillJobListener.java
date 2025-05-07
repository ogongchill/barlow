package com.barlow.app.batch.recentbill.job.listener;

import static com.barlow.app.batch.recentbill.RecentBillConstant.BATCH_DATE_JOB_PARAMETER;
import static com.barlow.app.batch.recentbill.RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.recentbill.job.TodayBillRetrieveClient;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.utils.HashUtil;
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
