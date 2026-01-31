package com.barlow.core.domain.registration;

import com.barlow.core.enumerate.AuthProvider;
import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

import java.util.Set;

public class RegistrationException extends CoreDomainException {

    protected RegistrationException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
        super(code, level, message);
    }

    protected RegistrationException(CoreDomainExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

    public static RegistrationException requiredTermsNotAccepted(Set<Long> missingTermIds) {
        String message = String.format("필수 동의 약관에 모두 동의하지 않았습니다. 누락된 약관 ID: %s", missingTermIds);
        return new RegistrationException(CoreDomainExceptionType.FORBIDDEN_EXCEPTION, message);
    }

    public static RegistrationException authProviderExists(AuthProvider authProvider) {
        return new RegistrationException(CoreDomainExceptionType.CONFLICT_EXCEPTION, authProvider.name() +"이 이미 계정에 등록되어 있습니다.");
    }

    public static RegistrationException oauthAlreadyRegistered() {
        return new RegistrationException(CoreDomainExceptionType.CONFLICT_EXCEPTION, "이미 다른 계정에 등록되어있습니다.");
    }
}
