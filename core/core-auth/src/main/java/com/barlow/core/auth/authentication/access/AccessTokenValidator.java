package com.barlow.core.auth.authentication.access;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barlow.core.auth.support.crypto.PublicKeyAlgorithm;
import com.barlow.core.auth.support.jwt.JwtConfig;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenValidator {

    private final JWTVerifier jwtVerifier;

    public AccessTokenValidator(PublicKeyAlgorithm publicKeyAlgorithm, JwtConfig accessTokenConfig) {
        this.jwtVerifier = JWT.require(publicKeyAlgorithm.getAlgorithm())
                .withIssuer(accessTokenConfig.getIssuer())
                .withClaimPresence(JwtConfig.Claims.MEMBER_NO.getName())
                .withClaimPresence(JwtConfig.Claims.ROLE.getName())
                .build();
    }

    public AccessTokenPayload getPayload(AccessToken token) {
        try {
            DecodedJWT jwt = jwtVerifier.verify(token.getValue());
            Claim memberNoClaim = jwt.getClaim(JwtConfig.Claims.MEMBER_NO.getName());
            Claim roleClaim = jwt.getClaim(JwtConfig.Claims.ROLE.getName());
            return new AccessTokenPayload(memberNoClaim.asLong(), roleClaim.asString());
        } catch (TokenExpiredException e) {
            throw AccessTokenException.expired(e.getExpiredOn().toEpochMilli());
        } catch (JWTVerificationException e) {
            throw AccessTokenException.invalid();
        }
    }
}
