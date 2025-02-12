package com.barlow.core.auth.authentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barlow.core.auth.support.crypto.PublicKeyAlgorithm;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenValidator {

    private final JWTVerifier jwtVerifier;

    public AccessTokenValidator(PublicKeyAlgorithm publicKeyAlgorithm) {
        this.jwtVerifier = JWT.require(publicKeyAlgorithm.getAlgorithm())
                .withIssuer("barlow")
                .build();
    }

    public MemberAccessPayload getPayload(Token token) {
        DecodedJWT jwt = jwtVerifier.verify(token.getValue());
        return new MemberAccessPayload(
                jwt.getClaim("memberNo").asLong(),
                jwt.getClaim("role").toString()
        );
    }
}
