package com.barlow.services.auth.authentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barlow.services.auth.DevelopTest;
import com.barlow.services.auth.authentication.core.AuthenticationException;
import com.barlow.services.auth.authentication.core.AuthenticationExceptionType;
import com.barlow.services.auth.authentication.core.MemberPrincipal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccessTokenAuthenticatorTest extends DevelopTest {

	@Autowired
	private Algorithm testPrivateKeyAlgorithm;

	@Autowired
	private	AccessTokenValidator accessTokenValidator;

	private AccessTokenAuthenticator accessTokenAuthenticator;

	@BeforeEach
	void setTokenAuthenticator() {
		accessTokenAuthenticator = new AccessTokenAuthenticator(accessTokenValidator);
	}

	@DisplayName("생성된 토큰을 통해 MemberPrincipal 을 반환하는지 확인")
	@Test
	void testAuthenticate() {
		MemberPrincipal expected = new MemberPrincipal(1L, "GUEST");
		String jwt = JWT.create()
			.withIssuer("barlow-core-auth")
			.withClaim("memberNo", expected.getMemberNo())
			.withClaim("role", expected.getRole())
			.withIssuedAt(Instant.now())
			.sign(testPrivateKeyAlgorithm);
		MemberPrincipal actual = accessTokenAuthenticator.authenticate(new AccessToken(jwt));
		assertThat(actual).isEqualTo(expected);
	}

	@DisplayName("jwt payload 에 iss, memberNo, role 중 하나라도 없으면 예외 확인")
	@ParameterizedTest
	@MethodSource("createMissingClaimPayload")
	void testThrowWhenMissingClaim(String payload) {
		String missingClaimJwt = JWT.create()
			.withPayload(payload)
			.sign(testPrivateKeyAlgorithm);
		AccessToken missingClaim = new AccessToken(missingClaimJwt);
		assertThatThrownBy(() -> accessTokenAuthenticator.authenticate(missingClaim))
			.isInstanceOf(AuthenticationException.class)
			.satisfies(this::verifyInvalidCredentialException);
	}

	private static Stream<Arguments> createMissingClaimPayload() {
		return Stream.of(
			Arguments.of("""
				{"iss":"barlow","memberNo":1}"""
			),
			Arguments.of("""
				{"iss":"barlow","role":"GUEST"}"""
			),
			Arguments.of("""
				{"role":"GUEST","memberNo":1}"""
			)
		);
	}

	@DisplayName("issuer 이름이 다르면 예외 확인")
	@Test
	void testThrowWhenInvalidIssuer() {
		String jwt = JWT.create()
			.withIssuer("wrong-issuer")
			.withClaim("role", "GUEST")
			.withClaim("memberNo", 1L)
			.sign(testPrivateKeyAlgorithm);
		AccessToken invalidIssuer = new AccessToken(jwt);
		assertThatThrownBy(() -> accessTokenAuthenticator.authenticate(invalidIssuer))
			.isInstanceOf(AuthenticationException.class)
			.satisfies(this::verifyInvalidCredentialException);
	}

	private void verifyInvalidCredentialException(Throwable e) {
		AuthenticationException exception = (AuthenticationException)e;
		assertThat(exception.getType()).isEqualTo(AuthenticationExceptionType.INVALID_CREDENTIAL);
	}

	@Test
	void testThrowWhenExpired() {
		String expiredJwt = JWT.create()
			.withIssuer("barlow-core-auth")
			.withClaim("role", "GUEST")
			.withClaim("memberNo", 1L)
			.withExpiresAt(Instant.now().minusSeconds(1000L))
			.sign(testPrivateKeyAlgorithm);
		AccessToken expiredToken = new AccessToken(expiredJwt);
		assertThatThrownBy(() -> accessTokenAuthenticator.authenticate(expiredToken))
			.isInstanceOf(AuthenticationException.class)
			.satisfies(this::verifyExpireException);
	}

	private void verifyExpireException(Throwable e) {
		AuthenticationException exception = (AuthenticationException)e;
		assertThat(exception.getType()).isEqualTo(AuthenticationExceptionType.EXPIRED_CREDENTIAL);
	}
}