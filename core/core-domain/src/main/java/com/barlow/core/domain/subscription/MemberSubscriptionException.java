package com.barlow.core.domain.subscription;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionType;

public class MemberSubscriptionException extends CoreDomainException {

	protected MemberSubscriptionException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}
}
