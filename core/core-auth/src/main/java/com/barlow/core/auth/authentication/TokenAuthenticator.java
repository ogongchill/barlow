package com.barlow.core.auth.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.barlow.core.auth.crypto.PublicKeyAlgorithm;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenAuthenticator {

    private final JWTVerifier verifier;

    public TokenAuthenticator(JWTVerifier verifier) {
        this.verifier = verifier;
    }

    public AuthenticationResult verify(String token) {
        try {
            String decodedJson = getPayloadFrom(token);
            return new AuthenticationResult(AuthenticationResultType.OK, decodedJson);
        } catch (TokenExpiredException e) {
            return new AuthenticationResult(AuthenticationResultType.EXPIRED, "");
        } catch (InvalidClaimException e) {
            return new AuthenticationResult(AuthenticationResultType.INVALID_ClAIM, "");
        }
    }

    private String getPayloadFrom(String token) {
        String encodedPayload = verifier.verify(token).getPayload();
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPayload);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private PublicKeyAlgorithm publicKeyAlgorithm;
        private String issuer;

        public Builder publicKeyAlgorithm(PublicKeyAlgorithm publicKeyAlgorithm) {
            this.publicKeyAlgorithm = publicKeyAlgorithm;
            return this;
        }

        public Builder issuer(String issuer) {
            this.issuer = issuer;
            return this;
        }

        public TokenAuthenticator build() {
            return new TokenAuthenticator(JWT.require(publicKeyAlgorithm.getAlgorithm())
                    .withIssuer(issuer)
                    .build());
        }
    }
}
