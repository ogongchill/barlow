package com.barlow.core.domain.reaction;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class ReactionDomainException extends CoreDomainException {

	protected ReactionDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	protected ReactionDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	static ReactionDomainException alreadyReact(String targetType, String targetId) {
		return new ReactionDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION,
			String.format("이미 리액션한 %s - %s 입니다. 리액션은 한 종류만 가능합니다", targetType, targetId)
		);
	}
}
