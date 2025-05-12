package com.barlow.services.auth.support.error;

public class CoreAuthErrorMessage {

	private final CoreAuthErrorType errorType;
	private final String message;
	private final Object data;

	public CoreAuthErrorMessage(CoreAuthErrorType errorType, String message) {
		this.errorType = errorType;
		this.message = message;
		this.data = null;
	}

	public CoreAuthErrorMessage(CoreAuthErrorType errorType, String message, Object data) {
		this.errorType = errorType;
		this.message = message;
		this.data = data;
	}

	public CoreAuthErrorType getErrorType() {
		return errorType;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}
}
