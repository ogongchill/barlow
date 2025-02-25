package com.barlow.batch.core.recentbill.job.listener;

import static com.barlow.batch.core.recentbill.RecentBillConstant.RECEIVED_BILL_WITH_FEW_PROPOSERS_SHARE_KEY;
import static com.barlow.batch.core.recentbill.RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.common.AbstractExecutionContextSharingManager;
import com.barlow.batch.core.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.batch.core.recentbill.job.TodayBillInfoResult;
import com.barlow.batch.core.utils.HashUtil;

@Component
@StepScope
public class BillProposerReaderStepExecutionContextSharingListener
	extends AbstractExecutionContextSharingManager
	implements StepExecutionListener {

	private final RecentBillJobScopeShareRepository jobScopeShareRepository;

	public BillProposerReaderStepExecutionContextSharingListener(
		RecentBillJobScopeShareRepository jobScopeShareRepository
	) {
		super();
		this.jobScopeShareRepository = jobScopeShareRepository;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.setCurrentExecutionContext(stepExecution.getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TODAY_BILL_INFO_SHARE_KEY);
		TodayBillInfoResult todayBillInfo = jobScopeShareRepository.findByKey(hashKey);

		TodayBillInfoResult receivedBillsWithFewProposers = todayBillInfo.filteredReceivedBillsWithFewProposers();
		String newHashKey = HashUtil.generate(receivedBillsWithFewProposers);

		super.setCurrentExecutionContext(stepExecution.getExecutionContext());
		super.putDataToExecutionContext(RECEIVED_BILL_WITH_FEW_PROPOSERS_SHARE_KEY, newHashKey);

		jobScopeShareRepository.save(newHashKey, receivedBillsWithFewProposers);
	}
}
