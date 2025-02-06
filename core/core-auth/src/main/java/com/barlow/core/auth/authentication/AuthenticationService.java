package com.barlow.core.auth.authentication;

import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final MemberTokenAuthenticator memberAuthenticator;

    public AuthenticationService(MemberTokenAuthenticator jwtAuthenticator) {
        this.memberAuthenticator = jwtAuthenticator;
    }

    public MemberAuthenticatorResult authenticateWithToken(String token) {
        return memberAuthenticator.authenticate(token);
    }
}
