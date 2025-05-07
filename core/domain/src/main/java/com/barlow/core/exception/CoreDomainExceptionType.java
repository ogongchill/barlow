package com.barlow.core.exception;

import static com.barlow.core.exception.CoreDomainExceptionCode.E403;
import static com.barlow.core.exception.CoreDomainExceptionCode.E404;
import static com.barlow.core.exception.CoreDomainExceptionCode.E409;
import static com.barlow.core.exception.CoreDomainExceptionLevel.IMPLEMENTATION;

public enum CoreDomainExceptionType {

	FORBIDDEN_EXCEPTION(E403, IMPLEMENTATION),
	NOT_FOUND_EXCEPTION(E404, IMPLEMENTATION),
	CONFLICT_EXCEPTION(E409, IMPLEMENTATION),
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
