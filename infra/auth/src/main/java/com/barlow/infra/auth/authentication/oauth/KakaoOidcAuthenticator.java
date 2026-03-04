package com.barlow.infra.auth.authentication.oauth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.barlow.infra.auth.authentication.core.AuthenticationException;
import com.barlow.infra.auth.authentication.core.AuthenticationExceptionType;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@Component
public class KakaoOidcAuthenticator implements OidcAuthenticator<KakaoIdToken> {

	private final JwkProvider kakaoJwkProvider;
	private final Map<String, String> kakaoOidcClaims;

	public KakaoOidcAuthenticator(JwkProvider kakaoJwkProvider, Map<String, String> kakaoOidcClaims) {
		this.kakaoJwkProvider = kakaoJwkProvider;
		this.kakaoOidcClaims = kakaoOidcClaims;
	}

	@Override
	public OidcPrincipal authenticate(KakaoIdToken kakaoIdToken) {
		try {
			DecodedJWT decoded = JWT.decode(kakaoIdToken.getValue());
			String keyId = decoded.getKeyId();
			Jwk jwk = kakaoJwkProvider.get(keyId);
			RSAPublicKey publicKey = (RSAPublicKey)jwk.getPublicKey();
			Algorithm algorithm = Algorithm.RSA256(publicKey, null);
			DecodedJWT jwt = verifyClaims(kakaoIdToken, algorithm);
			return OidcPrincipal.ofKakao().aud(jwt.getAudience().getFirst()).iss(jwt.getIssuer()).sub(jwt.getSubject())
				.nickname(jwt.getClaim("nickname").asString()).build();
		} catch (JwkException e) {
			throw new AuthenticationException(e.getMessage(), AuthenticationExceptionType.INVALID_CREDENTIAL);
		}
	}

	private DecodedJWT verifyClaims(KakaoIdToken kakaoIdToken, Algorithm algorithm) {
		JWTVerifier verifier = JWT.require(algorithm).withIssuer(kakaoOidcClaims.get("iss"))
			.withAudience(kakaoOidcClaims.get("aud")).build();
		return verifier.verify(kakaoIdToken.getValue());
	}
}
