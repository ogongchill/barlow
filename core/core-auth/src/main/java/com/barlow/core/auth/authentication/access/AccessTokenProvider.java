package com.barlow.core.auth.authentication.access;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.support.crypto.PrivateKeyAlgorithm;
import com.barlow.core.auth.support.jwt.JwtConfig;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProvider {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;
    private final JwtConfig accessTokenConfig;

    public AccessTokenProvider(PrivateKeyAlgorithm privateKeyAlgorithm, JwtConfig accessTokenConfig) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
        this.accessTokenConfig = accessTokenConfig;
    }

    public AccessToken issue(AccessTokenPayload memberInfo) {
        return new AccessToken(
                JWT.create()
                .withIssuer(accessTokenConfig.getIssuer())
                .withClaim(JwtConfig.Claims.MEMBER_NO.getName(), memberInfo.memberNo())
                .withClaim(JwtConfig.Claims.ROLE.getName(), memberInfo.role())
                .sign(privateKeyAlgorithm.getAlgorithm())
        );
    }
}
