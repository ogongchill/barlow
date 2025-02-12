package com.barlow.core.auth.authorization;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.support.crypto.PrivateKeyAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthorizer {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;

    public MemberAuthorizer(PrivateKeyAlgorithm privateKeyAlgorithm) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
    }

    public String sign(AuthorizableMemberInfo authorizableMemberInfo) {
        return JWT.create()
                .withIssuer("barlow")
                .withClaim("role", "GUEST")
                .sign(privateKeyAlgorithm.getAlgorithm());
    }
}
