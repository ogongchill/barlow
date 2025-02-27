package com.barlow.core.auth.authentication.token;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.authentication.core.MemberPrincipal;
import com.barlow.core.support.crypto.PrivateKeyAlgorithm;
import com.barlow.core.support.jwt.JwtConfig;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProvider {

    private final PrivateKeyAlgorithm privateKeyAlgorithm;
    private final JwtConfig accessTokenConfig;

    public AccessTokenProvider(PrivateKeyAlgorithm privateKeyAlgorithm, JwtConfig accessTokenConfig) {
        this.privateKeyAlgorithm = privateKeyAlgorithm;
        this.accessTokenConfig = accessTokenConfig;
    }

    public AccessToken issue(MemberPrincipal memberInfo) {
        return new AccessToken(
                JWT.create()
                .withIssuer(accessTokenConfig.getIssuer())
                .withClaim(JwtConfig.Claims.MEMBER_NO.getName(), memberInfo.getMemberNo())
                .withClaim(JwtConfig.Claims.ROLE.getName(), memberInfo.getRole())
                .sign(privateKeyAlgorithm.getAlgorithm())
        );
    }
}
