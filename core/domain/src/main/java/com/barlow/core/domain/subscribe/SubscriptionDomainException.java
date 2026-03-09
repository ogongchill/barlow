package com.barlow.core.domain.subscribe;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class SubscriptionDomainException extends CoreDomainException {

	public SubscriptionDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	public SubscriptionDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	public static SubscriptionDomainException alreadySubscribed(LegislationType legislationType) {
		return new SubscriptionDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION, String.format("이미 구독중인 입법계정 %s 입니다", legislationType));
	}

	public static SubscriptionDomainException alreadyUnSubscribed(LegislationType legislationType) {
		return new SubscriptionDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION, String.format("이미 구독 취소한 입법계정 %s 입니다", legislationType));
	}
}
