package com.barlow.core.auth.authentication;

import com.barlow.core.auth.jwt.Payload;

public record AuthenticationResult(
        AuthenticationResultType type,
        Payload payload
) {

    public boolean isOk() {
        return type.equals(AuthenticationResultType.OK);
    }

    public boolean isExpired() {
        return type.equals(AuthenticationResultType.EXPIRED);
    }
}
