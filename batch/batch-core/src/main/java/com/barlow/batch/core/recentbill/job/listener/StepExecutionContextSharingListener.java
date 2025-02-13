package com.barlow.batch.core.recentbill.job.listener;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

import com.barlow.batch.core.recentbill.job.AbstractExecutionContextSharingManager;

@Component
@StepScope
public class StepExecutionContextSharingListener
	extends AbstractExecutionContextSharingManager
	implements StepExecutionListener {

	public StepExecutionContextSharingListener() {
		super();
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
		super.setCurrentExecutionContext(stepExecution.getJobExecution().getExecutionContext());
		String hashKey = super.getDataFromJobExecutionContext(TODAY_BILL_INFO_JOB_KEY);

		super.setCurrentExecutionContext(stepExecution.getExecutionContext());
		super.putDataToExecutionContext(TODAY_BILL_INFO_JOB_KEY, hashKey);
	}
}
