package com.barlow.app.support;

import org.springframework.boot.test.context.TestComponent;

import com.barlow.infra.auth.authentication.token.AccessTokenProvider;
import com.barlow.core.domain.User;

@TestComponent
public class TestTokenProvider {

	private final AccessTokenProvider accessTokenProvider;

	public TestTokenProvider(AccessTokenProvider accessTokenProvider) {
		this.accessTokenProvider = accessTokenProvider;
	}

	public String getAccessTokenValue() {
		User user = User.of(1L, User.Role.GUEST);
		return accessTokenProvider.issue(user).getValue();
	}

	public String getAccessTokenValue(Long userNo, User.Role role) {
		User user = User.of(userNo, role);
		return accessTokenProvider.issue(user).getValue();
	}
}
