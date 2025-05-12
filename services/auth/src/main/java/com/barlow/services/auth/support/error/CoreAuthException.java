package com.barlow.services.auth.support.error;

import org.springframework.http.HttpStatus;

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

	public String getErrorMessage() {
		return errorMessage.getMessage();
	}

	public String getErrorCode() {
		return errorType.getCode().name();
	}

	public HttpStatus getErrorStatus() {
		return errorType.getStatus();
	}

	public Object getData() {
		return errorMessage.getData();
	}
}
