package com.barlow.core.domain.subscribe;

import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class SubscribeDomainException extends CoreDomainException {

	SubscribeDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	SubscribeDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	static SubscribeDomainException alreadySubscribed(LegislationType legislationType) {
		return new SubscribeDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION,
			String.format("이미 구독중인 입법계정 %s 입니다", legislationType)
		);
	}

	static SubscribeDomainException alreadyUnSubscribed(LegislationType legislationType) {
		return new SubscribeDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION,
			String.format("이미 구독 취소한 입법계정 %s 입니다", legislationType)
		);
	}
}
