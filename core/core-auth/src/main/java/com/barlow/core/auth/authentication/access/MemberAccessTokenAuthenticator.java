package com.barlow.core.auth.authentication.access;

import com.barlow.core.auth.authentication.core.AuthenticationRequest;
import com.barlow.core.auth.authentication.core.Authenticator;
import org.springframework.stereotype.Component;

@Component
public interface MemberAccessTokenAuthenticator extends Authenticator<Token, MemberPrincipal> {

    @Override
    TokenAuthenticationResult<MemberPrincipal> authenticate(AuthenticationRequest<Token> request);
}

