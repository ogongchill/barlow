package com.barlow.core.auth.authentication;

import com.auth0.jwt.JWT;
import com.barlow.core.auth.crypto.PrivateKeyAlgorithm;
import com.barlow.core.auth.crypto.PublicKeyAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.spec.InvalidKeySpecException;
import java.time.Instant;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TokenAuthenticatorTest {

    private static final String PRIVATE_KEY = """
            -----BEGIN PRIVATE KEY-----
            MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAInyqbBHBO3qJywS
            sVtKzv3yDZJLdsJwWzrBZE6+vYDJUia1598iJunWYNj8NKyhKn0Lg/MsOoVRIMae
            WM6vCn1zI5nnZMMKn4oBbejtLnOtgyNQ7LA2LAUwg8s2ia44+EJ1hC6Xk3hh/tgQ
            BlYp3VkJlEeT5tudJeHTk+bjLUV/AgMBAAECgYA9Zbzy0Vk3Tx1qN1Oq70vbqQ0z
            TNUpy3o/V23+wlRz6qbexd3S6U9qilkGmpITN5RCnYp4A/pz9dzhqf6a1zuSWkKU
            8N9n+TZbvWUCs0+K5j+zkc7n/Kocp0ArUkxQMpDwhBkVUsnSEGscAWDZB7z3DStk
            Hjy4G8rCQDtLp5694QJBAOGoLyN7syysX6pplqlzSOG/7C1LaKaS3lTmbfxSbLGN
            dgtXZOx2QfP12wIubMcwm3oMKSXYuqiugtZsViIUWw8CQQCcf0Jl/9sSPupPJYfT
            ChwCbP0LHztmiouFPRBk85gTE1KvaDNYvva19N6B/frzY7jqsE/8Ja6Bc8N4brMS
            Jy6RAkBWTcWhk4jmeBKqkXGe40mnlYiVljazJo7D99Fu5HNPwOO52LXvvhbhYFFf
            1zOhRxTfq9D8+ZQCOaQusAaNSC2/AkAkIRUZKzpOOgwp/YYf6KOSw8qfeLRj9fRD
            7FcOl1YygTGDoVDJWjzmwQNli1cWPZ2BQPcWRTTGWg10jkn1FOqhAkAWdtq6/LGr
            W21pF4R4cgS7vF7bf3ZLGqbgljAZLGXX77ncSdbF+U3zSk0Kf407bTFEuis8xPLh
            FqbpmG/RhdMe
            -----END PRIVATE KEY-----""";
    private static final String PUBLIC_KEY = """
            -----BEGIN PUBLIC KEY-----
            MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCJ8qmwRwTt6icsErFbSs798g2S
            S3bCcFs6wWROvr2AyVImteffIibp1mDY/DSsoSp9C4PzLDqFUSDGnljOrwp9cyOZ
            52TDCp+KAW3o7S5zrYMjUOywNiwFMIPLNomuOPhCdYQul5N4Yf7YEAZWKd1ZCZRH
            k+bbnSXh05Pm4y1FfwIDAQAB
            -----END PUBLIC KEY-----""";
    private PrivateKeyAlgorithm privateKeyAlgorithm;

    @BeforeEach
    void setPrivateKeyAlgorithm() throws InvalidKeySpecException {
        privateKeyAlgorithm = PrivateKeyAlgorithm.from(PRIVATE_KEY);
    }

    @DisplayName("jwt를 올바르게 decode하는지 확인")
    @Test
    void verify() throws InvalidKeySpecException {
        TokenAuthenticator authenticator = TokenAuthenticator.builder()
                .publicKeyAlgorithm(PublicKeyAlgorithm.from(PUBLIC_KEY))
                .issuer("issuer")
                .build();
        String jwt = JWT.create()
                .withIssuer("issuer")
                .withClaim("memberNo", "1")
                .sign(privateKeyAlgorithm.getAlgorithm());
        AuthenticationResult result = authenticator.verify(jwt);
        assertThat(result.isOk()).isTrue();
        assertThat(result.getPayload()).contains("\"memberNo\":\"1\"");
    }

    @DisplayName("issuer가 다를 시 검증 실패 확인")
    @Test
    void testIsNotOkWhenInvalidIssuer() throws InvalidKeySpecException {
        TokenAuthenticator authenticator = TokenAuthenticator.builder()
                .publicKeyAlgorithm(PublicKeyAlgorithm.from(PUBLIC_KEY))
                .issuer("issuer")
                .build();
        String jwt = JWT.create()
                .withIssuer("wrong issuer")
                .sign(privateKeyAlgorithm.getAlgorithm());
        AuthenticationResult result = authenticator.verify(jwt);
        assertThat(result.isOk()).isFalse();
    }

    @DisplayName("만료되 토큰일 시 검증 실패 확인")
    @Test
    void testIsNotOkWhenExpire() throws InvalidKeySpecException {
        TokenAuthenticator authenticator = TokenAuthenticator.builder()
                .publicKeyAlgorithm(PublicKeyAlgorithm.from(PUBLIC_KEY))
                .issuer("issuer")
                .build();
        String jwt = JWT.create()
                .withIssuer("issuer")
                .withExpiresAt(Date.from(Instant.now().minusSeconds(10L)))
                .sign(privateKeyAlgorithm.getAlgorithm());
        AuthenticationResult result = authenticator.verify(jwt);
        assertAll(
                () -> assertThat(result.isOk()).isFalse(),
                () -> assertThat(result.isExpired()).isTrue()
        );
    }
}