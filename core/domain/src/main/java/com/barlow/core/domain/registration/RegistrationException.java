package com.barlow.core.domain.registration;

import com.barlow.core.domain.account.AccountDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class RegistrationException extends AccountDomainException {

    protected RegistrationException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
        super(code, level, message);
    }

    protected RegistrationException(CoreDomainExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

    public static RegistrationException RequiredTermsNotAcceptedException() {
        return new RegistrationException(CoreDomainExceptionType.CONFLICT_EXCEPTION, "필수 동의 약관에 모두 동의하지 않았습니다.");
    }

    public static RegistrationException InvalidFlowStatusException(String message) {
        return new RegistrationException(CoreDomainExceptionType.CONFLICT_EXCEPTION, message);
    }
}
