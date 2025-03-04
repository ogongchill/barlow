package com.barlow.core.support;

import org.springframework.boot.test.context.TestComponent;

import com.barlow.core.auth.authentication.token.AccessTokenProvider;
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
}
