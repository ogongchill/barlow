package com.barlow.core.domain.version;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;

public class ClientVersionException extends CoreDomainException {

    ClientVersionException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
        super(code, level, message);
    }

    public static ClientVersionException invalidVersion(String message) {
        return new ClientVersionException(CoreDomainExceptionCode.E400, CoreDomainExceptionLevel.IMPLEMENTATION, message);
    }
}
