package com.barlow.core.exception;

public abstract class CoreDomainException extends RuntimeException {

	protected final CoreDomainExceptionCode code;
	protected final CoreDomainExceptionLevel level;
	protected final String message;

	protected CoreDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		this.code = code;
		this.level = level;
		this.message = message;
	}

	protected CoreDomainException(CoreDomainExceptionType exceptionType, String message) {
		this.code = exceptionType.getCode();
		this.level = exceptionType.getLevel();
		this.message = message;
	}

	public CoreDomainExceptionCode getCode() {
		return code;
	}

	public CoreDomainExceptionLevel getLevel() {
		return level;
	}

	@Override
	public String getMessage() {
		return message;
	}
}
