package com.barlow.core.domain.notification;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionType;

public class MemberNotificationConfigException extends CoreDomainException {

	MemberNotificationConfigException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}
}
