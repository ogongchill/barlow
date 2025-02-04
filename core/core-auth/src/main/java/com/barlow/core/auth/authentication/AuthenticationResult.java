package com.barlow.core.auth.authentication;

public record AuthenticationResult(
        AuthenticationResultType type,
        String payload
) {

    public boolean isOk() {
        return type.equals(AuthenticationResultType.OK);
    }

    public boolean isExpired() {
        return type.equals(AuthenticationResultType.EXPIRED);
    }

    public String getPayload() {
        if (!isOk()) {
            throw new IllegalStateException();
        }
        return payload;
    }
}
