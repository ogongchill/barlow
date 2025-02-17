package com.barlow.core.auth.authentication.core;

public class AuthenticationException extends RuntimeException {

    private final AuthenticationExceptionType type;

    public AuthenticationException(String message, AuthenticationExceptionType type) {
        super(message);
        this.type = type;
    }

    public AuthenticationExceptionType getType() {
        return type;
    }
}
