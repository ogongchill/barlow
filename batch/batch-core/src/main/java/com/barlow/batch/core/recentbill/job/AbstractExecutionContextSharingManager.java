package com.barlow.batch.core.recentbill.job;

import org.springframework.batch.item.ExecutionContext;

public abstract class AbstractExecutionContextSharingManager {

	public static final String TODAY_BILL_INFO_JOB_KEY = "TODAY_BILL_INFO";

	private ExecutionContext executionContext;

	protected AbstractExecutionContextSharingManager() {
	}

	protected void setCurrentExecutionContext(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	protected void putDataToExecutionContext(String key, String hashValue) {
		validateJobExecutionIsNull();
		executionContext.put(key, hashValue);
	}

	protected String getDataFromJobExecutionContext(String key) {
		validateJobExecutionIsNull();
		return (String)executionContext.get(key);
	}

	private void validateJobExecutionIsNull() {
		if (executionContext == null) {
			throw new IllegalStateException("ExecutionContext is null. Ensure that setCurrentJobExecution is called");
		}
	}
}
