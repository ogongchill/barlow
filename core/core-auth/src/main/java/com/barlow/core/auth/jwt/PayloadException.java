package com.barlow.core.auth.jwt;

import com.barlow.core.auth.exception.CoreAuthException;
import com.barlow.core.auth.exception.CoreAuthExceptionType;

public class PayloadException extends CoreAuthException {
    public PayloadException(CoreAuthExceptionType type, String message) {
        super(type, message);
    }
}
