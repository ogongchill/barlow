package com.barlow.batch.core.recentbill.job.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class StepLoggingListener implements StepExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(StepLoggingListener.class);
	private static final String BEFORE_MESSAGE = "{} Step 시작";
	private static final String AFTER_MESSAGE = "{} Step 종료 (Status: {})";

	@Override
	public void beforeStep(StepExecution stepExecution) {
		log.info(BEFORE_MESSAGE, stepExecution.getStepName());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (stepExecution.getStatus() == BatchStatus.FAILED) {
			log.error("{} Step 실패", stepExecution.getStepName());
		}
		log.info(AFTER_MESSAGE, stepExecution.getStepName(), stepExecution.getStatus());
		return stepExecution.getExitStatus();
	}
}
