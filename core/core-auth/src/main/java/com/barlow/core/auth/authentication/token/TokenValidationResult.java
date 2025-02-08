package com.barlow.core.auth.authentication.token;

public class TokenValidationResult<T> {

    private final T payload;
    private final TokenValidationResultType type;

    public TokenValidationResult(T payload, TokenValidationResultType result) {
        this.payload = payload;
        this.type = result;
    }

    public T getPayload() {
        return payload;
    }

    public TokenValidationResultType getType() {
        return type;
    }
}
