package com.barlow.core.auth.authentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barlow.core.auth.authentication.core.MemberPrincipal;
import com.barlow.core.auth.config.TestKeyConfig;
import com.barlow.core.support.crypto.AlgorithmConfig;
import com.barlow.core.support.jwt.TokenConfig;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(classes = {AccessTokenProvider.class, TokenConfig.class})
@Import(TestKeyConfig.class)
class AccessTokenProviderTest {

	@Autowired
	private AccessTokenProvider accessTokenProvider;

	@Autowired
	private Algorithm testPublicKeyAlgorithm;

	@DisplayName("jwt 생성이 되는지 확인")
	@Test()
	void testDoesNotThrowWhenIssue() {
		JWTVerifier verifier = JWT.require(testPublicKeyAlgorithm).build();
		AccessToken token = accessTokenProvider.issue(new MemberPrincipal(1L, "GUEST"));
		assertDoesNotThrow(() -> verifier.verify(token.getValue()));
	}

	@DisplayName("생성된 jwt의 claim이 올바른지 확인")
	@Test()
	void testIssue() {
		JWTVerifier verifier = JWT.require(testPublicKeyAlgorithm).build();
		AccessToken token = accessTokenProvider.issue(new MemberPrincipal(1L, "GUEST"));
		DecodedJWT decodedJWT = verifier.verify(token.getValue());
		assertAll(
			() -> assertThat(decodedJWT.getIssuer()).isEqualTo("barlow"),
			() -> assertThat(decodedJWT.getClaim("role").asString()).isEqualTo("GUEST"),
			() -> assertThat(decodedJWT.getClaim("memberNo").asLong()).isEqualTo(1L)
		);
	}
}