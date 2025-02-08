package com.barlow.support.jwt.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.*;
import com.barlow.core.auth.authentication.token.*;
import com.barlow.support.jwt.crypto.PublicKeyAlgorithm;
import com.barlow.support.jwt.exception.JwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class MemberAuthenticatorAdapter implements MemberTokenValidator {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final JWTVerifier verifier;

    public MemberAuthenticatorAdapter(PublicKeyAlgorithm publicKeyAlgorithm) {
        this.verifier = JWT.require(publicKeyAlgorithm.getAlgorithm())
                .withIssuer("barlow")
                .build();
    }

    @Override
    public TokenValidationResult<MemberAccessPayload> validate(Token token) {
        try {
            String encodedPayload = verifier.verify(token.getValue()).getPayload();
            MemberAccessPayload payload = parse(encodedPayload);
            return new TokenValidationResult<>(payload, TokenValidationResultType.SUCCESS);
        } catch (JWTVerificationException e) {
            return handleJWTVerificationException(e);
        }
    }

    private MemberAccessPayload parse(String encodedPayload) {
        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedPayload);
            String payloadJson = new String(decodedBytes, StandardCharsets.UTF_8);
            return MAPPER.readValue(payloadJson, MemberAccessPayload.class);
        } catch (JsonProcessingException e) {
                throw JwtException.cannotParsePayload(e);
        }
    }

    private TokenValidationResult<MemberAccessPayload> handleJWTVerificationException(JWTVerificationException e) {
        if (e instanceof TokenExpiredException) {
            return new TokenValidationResult<>(MemberAccessPayload.invalid(), TokenValidationResultType.EXPIRED);
        }
        return new TokenValidationResult<>(MemberAccessPayload.invalid(), TokenValidationResultType.FAILURE);
    }
}
