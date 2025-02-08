package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.token.Token;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthenticationService {

    private final MemberAccessTokenAuthenticator memberTokenAuthenticator;

    public MemberAuthenticationService(MemberAccessTokenAuthenticator memberTokenAuthenticator) {
        this.memberTokenAuthenticator = memberTokenAuthenticator;
    }

    public MemberPrincipal authenticate(Token token) {
        return memberTokenAuthenticator.authenticate(token);
    }
}
