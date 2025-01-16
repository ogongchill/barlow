package com.barlow.core.domain.subscription;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionType;

public class MemberSubscriptionException extends CoreDomainException {

    protected MemberSubscriptionException(CoreDomainExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

    static MemberSubscriptionException memberMismatchException(String message) {
        throw new MemberSubscriptionException(
                CoreDomainExceptionType.ILLEGAL_STATE_EXCEPTION,
                message
        );
    }
}
