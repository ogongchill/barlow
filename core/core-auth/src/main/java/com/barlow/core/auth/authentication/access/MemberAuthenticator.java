package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.Authenticator;
import com.barlow.core.auth.authentication.token.*;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticator implements Authenticator<Token, MemberPrincipal> {

    private final AccessTokenValidator memberTokenValidator;

    public MemberAuthenticator(AccessTokenValidator memberTokenValidator) {
        this.memberTokenValidator = memberTokenValidator;
    }

    @Override
    public MemberPrincipal authenticate(Token token) {
        MemberAccessPayload payload = memberTokenValidator.getPayload(token);
        return new MemberPrincipal(payload.memberNo(), payload.role());
    }
}

