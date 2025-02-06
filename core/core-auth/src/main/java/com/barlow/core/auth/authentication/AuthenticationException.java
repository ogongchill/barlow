package com.barlow.core.auth.authentication;

import com.barlow.core.auth.exception.CoreAuthException;
import com.barlow.core.auth.exception.CoreAuthExceptionType;

public class AuthenticationException extends CoreAuthException {

    public AuthenticationException(CoreAuthExceptionType type, String message) {
        super(type, message);
    }

    public static AuthenticationException algorithmMismatch(String message) {
        return new AuthenticationException(CoreAuthExceptionType.ALGORITHM_MISMATCH, message);
    }
}
