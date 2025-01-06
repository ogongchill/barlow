package com.barlow.core.exception;

import static com.barlow.core.exception.CoreDomainExceptionCode.E404;
import static com.barlow.core.exception.CoreDomainExceptionLevel.IMPLEMENTATION;

public enum CoreDomainExceptionType {

	NOT_FOUND_EXCEPTION(E404, IMPLEMENTATION),
	;

	private final CoreDomainExceptionCode code;
	private final CoreDomainExceptionLevel level;

	CoreDomainExceptionType(CoreDomainExceptionCode code, CoreDomainExceptionLevel level) {
		this.code = code;
		this.level = level;
	}

	public CoreDomainExceptionCode getCode() {
		return code;
	}

	public CoreDomainExceptionLevel getLevel() {
		return level;
	}
}
