package com.barlow.core.auth.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.IncorrectClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.barlow.core.auth.crypto.PublicKeyAlgorithm;
import com.barlow.core.auth.jwt.Payload;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenAuthenticator {

    private final JWTVerifier verifier;

    protected TokenAuthenticator(JWTVerifier verifier) {
        this.verifier = verifier;
    }

    public AuthenticationResult authenticate(String token) {
        try {
            String payloadJson = getPayloadFrom(token);
            Payload payload = Payload.fromJson(payloadJson);
            return new AuthenticationResult(AuthenticationResultType.OK, payload);
        } catch (IncorrectClaimException e) {
            return new AuthenticationResult(AuthenticationResultType.INVALID_ClAIM, Payload.empty());
        } catch (TokenExpiredException e) {
            return new AuthenticationResult(AuthenticationResultType.EXPIRED, Payload.empty());
        } catch (AlgorithmMismatchException e) {
            throw AuthenticationException.algorithmMismatch(e.getMessage());
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
