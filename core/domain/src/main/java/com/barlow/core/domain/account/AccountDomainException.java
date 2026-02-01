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

	public static AccountDomainException modificationException(String message) {
		return new AccountDomainException(CoreDomainExceptionType.CONFLICT_EXCEPTION, message);
	}

	public static AccountDomainException accountNotFound() {
		return new AccountDomainException(CoreDomainExceptionType.NOT_FOUND_EXCEPTION, "계정이 존재하지 않습니다");
	}

	public static AccountDomainException notFound(String message) {
		return new AccountDomainException(CoreDomainExceptionType.NOT_FOUND_EXCEPTION, message);
	}

	public static AccountDomainException forbidden(String message) {
		return new AccountDomainException(CoreDomainExceptionType.FORBIDDEN_EXCEPTION, message);
	}
}
