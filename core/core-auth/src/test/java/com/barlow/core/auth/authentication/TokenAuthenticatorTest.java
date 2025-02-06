//package com.barlow.core.auth.authentication;
//
//import com.auth0.jwt.JWT;
//import com.barlow.core.auth.crypto.PrivateKeyAlgorithm;
//import com.barlow.core.auth.crypto.PublicKeyAlgorithm;
//import com.barlow.core.auth.jwt.TokenAuthenticator;
//import com.barlow.core.auth.utils.TestKeys;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//import java.security.spec.InvalidKeySpecException;
//import java.time.Instant;
//import java.util.Date;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//class TokenAuthenticatorTest {
//
//    private PrivateKeyAlgorithm privateKeyAlgorithm;
//
//    @BeforeEach
//    void setPrivateKeyAlgorithm() throws InvalidKeySpecException {
//        privateKeyAlgorithm = PrivateKeyAlgorithm.from(TestKeys.PRIVATE.getKeyString());
//    }
//
//    @DisplayName("jwt를 올바르게 decode하는지 확인")
//    @Test
//    void verify() throws InvalidKeySpecException {
//        TokenAuthenticator authenticator = TokenAuthenticator.builder()
//                .publicKeyAlgorithm(PublicKeyAlgorithm.from(TestKeys.PUBLIC.getKeyString()))
//                .issuer("issuer")
//                .build();
//        String jwt = JWT.create()
//                .withIssuer("issuer")
//                .withClaim("memberNo", "1")
//                .sign(privateKeyAlgorithm.getAlgorithm());
//        AuthenticationResult result = authenticator.authenticate(jwt);
//        assertAll(
//                () -> assertThat(result.isOk()).isTrue()
//        );
//    }
//
//    @DisplayName("issuer가 다를 시 검증 실패 확인")
//    @Test
//    void testIsNotOkWhenInvalidIssuer() throws InvalidKeySpecException {
//        TokenAuthenticator authenticator = TokenAuthenticator.builder()
//                .publicKeyAlgorithm(PublicKeyAlgorithm.from(TestKeys.PUBLIC.getKeyString()))
//                .issuer("issuer")
//                .build();
//        String jwt = JWT.create()
//                .withIssuer("wrong issuer")
//                .sign(privateKeyAlgorithm.getAlgorithm());
//        AuthenticationResult result = authenticator.authenticate(jwt);
//        assertThat(result.isOk()).isFalse();
//    }
//
//    @DisplayName("만료되 토큰일 시 검증 실패 확인")
//    @Test
//    void testIsNotOkWhenExpire() throws InvalidKeySpecException {
//        TokenAuthenticator authenticator = TokenAuthenticator.builder()
//                .publicKeyAlgorithm(PublicKeyAlgorithm.from(TestKeys.PUBLIC.getKeyString()))
//                .issuer("issuer")
//                .build();
//        String jwt = JWT.create()
//                .withIssuer("issuer")
//                .withExpiresAt(Date.from(Instant.now().minusSeconds(10L)))
//                .sign(privateKeyAlgorithm.getAlgorithm());
//        AuthenticationResult result = authenticator.authenticate(jwt);
//        assertAll(
//                () -> assertThat(result.isOk()).isFalse(),
//                () -> assertThat(result.isExpired()).isTrue()
//        );
//    }
//}