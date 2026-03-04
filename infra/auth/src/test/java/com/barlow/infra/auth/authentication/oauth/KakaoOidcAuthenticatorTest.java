package com.barlow.infra.auth.authentication.oauth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.barlow.infra.auth.authentication.core.AuthenticationException;
import com.barlow.infra.auth.authentication.core.AuthenticationExceptionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class KakaoOidcAuthenticatorTest {

    private static final String KAKAO_ISS = "https://kauth.kakao.com";

    // 에러 케이스 테스트용 설정
    private static final String TEST_KID = "test-key-id";
    private static final String TEST_AUD = "test_aud";
    private static RSAPublicKey rsaPublicKey;
    private static Algorithm rsaAlgorithm;

    @Mock
    private JwkProvider jwkProvider;

    @Mock
    private Jwk jwk;

    @BeforeAll
    static void generateRsaKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair keyPair = generator.generateKeyPair();
        rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        rsaAlgorithm = Algorithm.RSA256(rsaPublicKey, (RSAPrivateKey) keyPair.getPrivate());
    }

    @DisplayName("유효한 ID 토큰 검증 성공 시 OidcPrincipal 반환")
    @Test
    void testAuthenticateSuccess() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        String sub = "test_subject_123";
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer(KAKAO_ISS)
                .withAudience(TEST_AUD)
                .withSubject(sub)
                .withIssuedAt(Instant.now())
                .withExpiresAt(Instant.now().plusSeconds(3600))
                .withClaim("nickname", "testNickname")
                .sign(rsaAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenReturn(jwk);
        when(jwk.getPublicKey()).thenReturn(rsaPublicKey);

        OidcPrincipal principal = authenticator.authenticate(token);

        assertAll(
                () -> assertThat(principal.getIss()).isEqualTo(KAKAO_ISS),
                () -> assertThat(principal.getAud()).isEqualTo(TEST_AUD),
                () -> assertThat(principal.getSub()).isEqualTo(sub),
                () -> assertThat(principal.getOauthProvider()).isEqualTo(OauthProvider.KAKAO)
        );
    }

    @DisplayName("JWK 조회 실패 시 INVALID_CREDENTIAL 예외 발생")
    @Test
    void testThrowWhenJwkException() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer(KAKAO_ISS)
                .withAudience(TEST_AUD)
                .withIssuedAt(Instant.now())
                .sign(rsaAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenThrow(new JwkException("JWK not found"));

        assertThatThrownBy(() -> authenticator.authenticate(token))
                .isInstanceOfSatisfying(AuthenticationException.class, e ->
                        assertThat(e.getType()).isEqualTo(AuthenticationExceptionType.INVALID_CREDENTIAL));
    }

    @DisplayName("잘못된 issuer 토큰 검증 실패")
    @Test
    void testThrowWhenInvalidIssuer() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer("https://wrong-issuer.com")
                .withAudience(TEST_AUD)
                .withIssuedAt(Instant.now())
                .sign(rsaAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenReturn(jwk);
        when(jwk.getPublicKey()).thenReturn(rsaPublicKey);

        assertThatThrownBy(() -> authenticator.authenticate(token))
                .isInstanceOf(JWTVerificationException.class);
    }

    @DisplayName("잘못된 audience 토큰 검증 실패")
    @Test
    void testThrowWhenInvalidAudience() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer(KAKAO_ISS)
                .withAudience("wrong_aud")
                .withIssuedAt(Instant.now())
                .sign(rsaAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenReturn(jwk);
        when(jwk.getPublicKey()).thenReturn(rsaPublicKey);

        assertThatThrownBy(() -> authenticator.authenticate(token))
                .isInstanceOf(JWTVerificationException.class);
    }

    @DisplayName("만료된 토큰 검증 실패")
    @Test
    void testThrowWhenExpiredToken() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer(KAKAO_ISS)
                .withAudience(TEST_AUD)
                .withExpiresAt(Instant.now().minusSeconds(1000))
                .sign(rsaAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenReturn(jwk);
        when(jwk.getPublicKey()).thenReturn(rsaPublicKey);

        assertThatThrownBy(() -> authenticator.authenticate(token))
                .isInstanceOf(JWTVerificationException.class);
    }

    @DisplayName("다른 키로 서명된 토큰 검증 실패")
    @Test
    void testThrowWhenInvalidSignature() throws Exception {
        KakaoOidcAuthenticator authenticator = new KakaoOidcAuthenticator(jwkProvider, buildClaims(TEST_AUD));
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        KeyPair differentKeyPair = generator.generateKeyPair();
        Algorithm differentAlgorithm = Algorithm.RSA256(
                (RSAPublicKey) differentKeyPair.getPublic(),
                (RSAPrivateKey) differentKeyPair.getPrivate()
        );
        String idToken = JWT.create()
                .withKeyId(TEST_KID)
                .withIssuer(KAKAO_ISS)
                .withAudience(TEST_AUD)
                .withIssuedAt(Instant.now())
                .sign(differentAlgorithm);
        KakaoIdToken token = KakaoIdToken.of(idToken);

        when(jwkProvider.get(TEST_KID)).thenReturn(jwk);
        when(jwk.getPublicKey()).thenReturn(rsaPublicKey);

        assertThatThrownBy(() -> authenticator.authenticate(token))
                .isInstanceOf(JWTVerificationException.class);
    }

    private Map<String, String> buildClaims(String aud) {
        Map<String, String> claims = new HashMap<>();
        claims.put("iss", KAKAO_ISS);
        claims.put("aud", aud);
        return claims;
    }
}
