package com.barlow.core.auth.authentication;

public class AuthenticationResult<T> {

    private final AuthenticationResultType type;
    private final T data;

    public AuthenticationResult(AuthenticationResultType resultType, T data) {
        this.type = resultType;
        this.data = data;
    }

    public boolean isOk() {
        return type.equals(AuthenticationResultType.OK);
    }

    public boolean isExpired() {
        return type.equals(AuthenticationResultType.EXPIRED);
    }

    public T getData() {
        if(!isOk()) {
            throw new IllegalArgumentException("");
        }
        return data;
    }
}
