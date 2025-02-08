package com.barlow.support.jwt.token;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.authorization.AuthorizableMemberInfo;
import com.barlow.core.auth.authorization.MemberAuthorizer;
import com.barlow.support.jwt.crypto.PrivateKeyAlgorithm;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class MemberAuthorizerAdapter implements MemberAuthorizer {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;

    public MemberAuthorizerAdapter(PrivateKeyAlgorithm privateKeyAlgorithm) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
    }

    public String sign(AuthorizableMemberInfo memberInfo) {
        return JWT.create()
                .withSubject(memberInfo.memberNo())
                .withIssuedAt(Date.from(Instant.now()))
                .withIssuer("barlow")
                .withClaim("role", memberInfo.role())
                .sign(privateKeyAlgorithm.getAlgorithm());
    }
}
