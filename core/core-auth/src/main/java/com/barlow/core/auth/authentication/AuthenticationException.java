package com.barlow.core.auth.authentication;

public class AuthenticationException extends RuntimeException{

    private final AuthenticationResultType resultType;

    public AuthenticationException(AuthenticationResultType resultType, String message) {
        super(message);
        this.resultType = resultType;
    }
}
