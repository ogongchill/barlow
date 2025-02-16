package com.barlow.core.auth.authentication.access;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.support.crypto.PrivateKeyAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProvider {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;

    public AccessTokenProvider(PrivateKeyAlgorithm privateKeyAlgorithm) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
    }

    public AccessToken issue(AccessTokenPayload memberInfo) {
        return new AccessToken(
                JWT.create()
                .withSubject(memberInfo.memberNo().toString())
                .withIssuer("barlow")
                .withClaim("role", memberInfo.role())
                .sign(privateKeyAlgorithm.getAlgorithm())
        );
    }
}
