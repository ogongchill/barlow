package com.barlow.core.support.error;

public class CoreAuthException extends RuntimeException {

	private final CoreAuthErrorType errorType;
	private final CoreAuthErrorMessage errorMessage;

	public CoreAuthException(CoreAuthErrorType errorType, String errorMessage) {
		super(errorMessage);
		this.errorType = errorType;
		this.errorMessage = new CoreAuthErrorMessage(errorType, errorMessage);
	}

	public CoreAuthException(CoreAuthErrorType errorType, String errorMessage, Object data) {
		super(errorMessage);
		this.errorType = errorType;
		this.errorMessage = new CoreAuthErrorMessage(errorType, errorMessage, data);
	}

	public CoreAuthErrorType getErrorType() {
		return errorType;
	}

	public CoreAuthErrorMessage getErrorMessage() {
		return errorMessage;
	}
}
