package com.barlow.core.support.error;

public class CoreApiException extends RuntimeException {

	private final CoreApiErrorType errorType;
	private final Object data;

	public CoreApiException(CoreApiErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = null;
	}

	public CoreApiException(CoreApiErrorType errorType, Object data) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = data;
	}

	public CoreApiErrorType getErrorType() {
		return errorType;
	}

	public Object getData() {
		return data;
	}
}
