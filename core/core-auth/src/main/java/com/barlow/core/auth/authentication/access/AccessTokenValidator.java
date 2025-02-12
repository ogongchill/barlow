package com.barlow.core.auth.authentication.access;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
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

    public AccessTokenPayload getPayload(AccessToken token) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(token.getValue());
            return new AccessTokenPayload(
                    jwt.getClaim("memberNo").asLong(),
                    jwt.getClaim("role").toString()
            );
        } catch (TokenExpiredException e) {
            throw AccessTokenException.expired(e.getExpiredOn().toEpochMilli());
        } catch (JWTVerificationException e) {
            throw AccessTokenException.invalid();
        }
    }
}
