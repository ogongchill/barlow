package com.barlow.support.jwt.token;

import com.auth0.jwt.exceptions.*;
import com.barlow.core.auth.authentication.core.AuthenticationRequest;
import com.barlow.core.auth.authentication.access.MemberPrincipal;
import com.barlow.core.auth.authentication.access.MemberAccessTokenAuthenticator;
import com.barlow.core.auth.authentication.access.Token;
import com.barlow.core.auth.authentication.access.TokenAuthenticationResult;
import com.barlow.core.auth.authentication.access.TokenVerificationResult;
import com.barlow.support.jwt.exception.JwtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class MemberAuthenticatorAdapter implements MemberAccessTokenAuthenticator {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final MemberTokenProvider memberTokenProvider;

    public MemberAuthenticatorAdapter(MemberTokenProvider memberTokenProvider) {
        this.memberTokenProvider = memberTokenProvider;
    }

    public TokenAuthenticationResult<MemberPrincipal> authenticate(AuthenticationRequest<Token> request) {
        try {
            String payloadJson = memberTokenProvider.getPayload(request.getCredentials().getValue());
            MemberPrincipal principal = MAPPER.readValue(payloadJson, MemberPrincipal.class);
            return new TokenAuthenticationResult<>(principal, TokenVerificationResult.SUCCESS);
        } catch (JWTVerificationException e) {
            return handleJWTVerificationException(e);
        } catch (JsonProcessingException e) {
            throw JwtException.cannotParsePayload(e);
        }
    }

    private TokenAuthenticationResult<MemberPrincipal> handleJWTVerificationException(JWTVerificationException e) {
        if (e instanceof AlgorithmMismatchException exception) {
            throw JwtException.algorithmMismatch(exception);
        }
        if (e instanceof TokenExpiredException) {
            return new TokenAuthenticationResult<>(MemberPrincipal.notAuthenticated(), TokenVerificationResult.EXPIRED);
        }
        return new TokenAuthenticationResult<>(MemberPrincipal.notAuthenticated(), TokenVerificationResult.FAILURE);
    }
}
