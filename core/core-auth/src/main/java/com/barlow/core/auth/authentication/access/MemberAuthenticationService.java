package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.AuthenticationRequest;
import org.springframework.stereotype.Service;

@Service
public class MemberAuthenticationService {

    private final MemberAccessTokenAuthenticator memberTokenAuthenticator;

    public MemberAuthenticationService(MemberAccessTokenAuthenticator memberTokenAuthenticator) {
        this.memberTokenAuthenticator = memberTokenAuthenticator;
    }

    public TokenAuthenticationResult<MemberPrincipal> authenticateToken(Token token) {
        AuthenticationRequest<Token> request = new AuthenticationRequest<>(token);
        return memberTokenAuthenticator.authenticate(request);
    }
}
