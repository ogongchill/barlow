package com.barlow.batch.core.recentbill.job.listener;

import java.time.LocalDate;

import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.NationalAssemblyLegislationClient;
import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.batch.core.recentbill.job.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.batch.core.utils.HashUtil;

@Component
public class RetrieveTodayBillJobListener
	extends AbstractExecutionContextSharingManager
	implements JobExecutionListener {

	private final NationalAssemblyLegislationClient client;
	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public RetrieveTodayBillJobListener(
		NationalAssemblyLegislationClient client,
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.client = client;
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public void beforeJob(@NotNull JobExecution jobExecution) {
		LocalDate batchDate = jobExecution.getJobParameters().getLocalDate("batchDate");
		TodayBillInfoResult todayBillInfo = client.getTodayBillInfo(batchDate);

		String hashKey = HashUtil.generate(todayBillInfo);
		super.setCurrentExecutionContext(jobExecution.getExecutionContext());
		super.putDataToExecutionContext(TODAY_BILL_INFO_JOB_KEY, hashKey);

		jobScopeShareRepository.save(hashKey, todayBillInfo);
	}
}
