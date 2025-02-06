package com.barlow.core.auth.authentication.core;

public abstract class AuthenticationResult<T extends Principal> {

    private final T principal;

    protected AuthenticationResult(T principal) {
        this.principal = principal;
    }

    protected abstract boolean isSuccess();

    public T getPrincipal() {
        if(!isSuccess()) {
            throw new IllegalArgumentException("");
        }
        return principal;
    }
}
