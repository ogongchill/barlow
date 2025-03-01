package com.barlow.core.auth.authentication.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.barlow.core.auth.authentication.core.MemberPrincipal;
import com.barlow.core.auth.config.JwtConfig;
import com.barlow.core.domain.User;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenProvider {

	private final Algorithm privateKeyAlgorithm;
	private final JwtConfig accessTokenConfig;

	public AccessTokenProvider(
		@Qualifier("jwtPrivateKeyAlgorithm") Algorithm privateKeyAlgorithm,
		@Qualifier("accessTokenConfig") JwtConfig accessTokenConfig
	) {
		this.privateKeyAlgorithm = privateKeyAlgorithm;
		this.accessTokenConfig = accessTokenConfig;
	}

	public AccessToken issue(MemberPrincipal memberInfo) {
		return new AccessToken(
			JWT.create()
				.withIssuer(accessTokenConfig.getIssuer())
				.withClaim(JwtConfig.Claims.MEMBER_NO.getName(), memberInfo.getMemberNo())
				.withClaim(JwtConfig.Claims.ROLE.getName(), memberInfo.getRole())
				.sign(privateKeyAlgorithm)
		);
	}

	public AccessToken issue(User user) {
		return new AccessToken(
			JWT.create()
				.withIssuer(accessTokenConfig.getIssuer())
				.withClaim(JwtConfig.Claims.MEMBER_NO.getName(), user.getUserNo())
				.withClaim(JwtConfig.Claims.ROLE.getName(), user.getRoleName())
				.sign(privateKeyAlgorithm)
		);
	}
}
