package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.AuthenticationResult;
import com.barlow.core.auth.authentication.core.Principal;

public class TokenAuthenticationResult<T extends Principal> extends AuthenticationResult<T> {

    private final TokenVerificationResult type;

    public TokenAuthenticationResult(T principal, TokenVerificationResult tokenVerificationResult) {
        super(principal);
        this.type = tokenVerificationResult;
    }

    @Override
    public boolean isSuccess() {
        return type.equals(TokenVerificationResult.SUCCESS);
    }

    public boolean isExpired() {
        return type.equals(TokenVerificationResult.EXPIRED);
    }
}
