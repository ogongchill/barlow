package com.barlow.app.batch.common;

import org.springframework.batch.item.ExecutionContext;

public abstract class AbstractExecutionContextSharingManager {

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
