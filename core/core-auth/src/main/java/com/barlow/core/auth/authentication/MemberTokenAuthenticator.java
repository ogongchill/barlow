package com.barlow.core.auth.authentication;

import org.springframework.stereotype.Component;

@Component
public interface MemberTokenAuthenticator {

    MemberAuthenticatorResult authenticate(String token);
}

