package com.barlow.core.auth.exception;

public class CoreAuthException extends RuntimeException {

    private final CoreAuthExceptionType type;

    public CoreAuthException(CoreAuthExceptionType type, String message) {
        super(message);
        this.type = type;
    }

    public CoreAuthExceptionType getType() {
        return type;
    }
}
