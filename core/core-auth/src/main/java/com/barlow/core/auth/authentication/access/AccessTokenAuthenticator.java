package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.Authenticator;
import com.barlow.core.auth.authentication.core.MemberPrincipal;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenAuthenticator implements Authenticator<AccessToken, MemberPrincipal> {

    private final AccessTokenValidator memberTokenValidator;

    public AccessTokenAuthenticator(AccessTokenValidator memberTokenValidator) {
        this.memberTokenValidator = memberTokenValidator;
    }

    @Override
    public MemberPrincipal authenticate(AccessToken token) {
        AccessTokenPayload payload = memberTokenValidator.getPayload(token);
        return new MemberPrincipal(payload.memberNo(), payload.role());
    }
}

