package com.barlow.core.support.error;

public class CoreApiErrorMessage {

	private final String code;
	private final String message;
	private final Object data;

	public CoreApiErrorMessage(String code, String message, Object data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public CoreApiErrorMessage(CoreApiErrorType errorType) {
		this.code = errorType.getCode().name();
		this.message = errorType.getMessage();
		this.data = null;
	}

	public CoreApiErrorMessage(CoreApiErrorType errorType, Object data) {
		this.code = errorType.getCode().name();
		this.message = errorType.getMessage();
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public Object getData() {
		return data;
	}
}
