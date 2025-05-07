package com.barlow.app.batch.recentbill.job.listener;

import static com.barlow.app.batch.recentbill.RecentBillConstant.BILL_WITH_FEW_PROPOSERS_SHARE_KEY;
import static com.barlow.app.batch.recentbill.RecentBillConstant.TODAY_BILL_INFO_SHARE_KEY;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.barlow.app.batch.common.AbstractExecutionContextSharingManager;
import com.barlow.app.batch.recentbill.job.RecentBillJobScopeShareRepository;
import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.utils.HashUtil;

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
		TodayBillInfoBatchEntity todayBillInfo = jobScopeShareRepository.findByKey(hashKey);

		TodayBillInfoBatchEntity billsWithFewProposers = todayBillInfo.filteredBillsWithFewProposers();
		String newHashKey = HashUtil.generate(billsWithFewProposers);

		super.setCurrentExecutionContext(stepExecution.getExecutionContext());
		super.putDataToExecutionContext(BILL_WITH_FEW_PROPOSERS_SHARE_KEY, newHashKey);

		jobScopeShareRepository.save(newHashKey, billsWithFewProposers);
	}
}
