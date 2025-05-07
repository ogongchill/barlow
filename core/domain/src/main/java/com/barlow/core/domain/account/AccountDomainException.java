package com.barlow.core.domain.account;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class AccountDomainException extends CoreDomainException {

	protected AccountDomainException(CoreDomainExceptionCode code,
		CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	protected AccountDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}
}
