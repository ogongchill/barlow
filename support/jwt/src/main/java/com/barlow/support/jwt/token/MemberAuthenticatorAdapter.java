package com.barlow.support.jwt.token;

import com.auth0.jwt.exceptions.*;
import com.barlow.core.auth.authentication.*;
import com.barlow.support.jwt.exception.JwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticatorAdapter implements MemberTokenAuthenticator {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final MemberTokenProvider memberTokenProvider;

    public MemberAuthenticatorAdapter(MemberTokenProvider memberTokenProvider) {
        this.memberTokenProvider = memberTokenProvider;
    }

    @Override
    public MemberAuthenticatorResult authenticate(String token) {
        try {
            String payloadJson = memberTokenProvider.getPayload(token);
            AuthenticatedMember data = MAPPER.readValue(payloadJson, AuthenticatedMember.class);
            return MemberAuthenticatorResult.ok(data);
        } catch (JWTVerificationException e) {
            return handleJWTVerificationException(e);
        } catch (JsonProcessingException e) {
            throw JwtException.cannotParsePayload(e);
        }
    }

    private MemberAuthenticatorResult handleJWTVerificationException(JWTVerificationException e) {
        if(e instanceof AlgorithmMismatchException exception) {
            throw JwtException.algorithmMismatch(exception);
        }
        if(e instanceof TokenExpiredException) {
            return MemberAuthenticatorResult.expired();
        }
        return MemberAuthenticatorResult.invalid();
    }
}
