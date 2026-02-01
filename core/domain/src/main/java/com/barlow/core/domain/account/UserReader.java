package com.barlow.core.domain.account;

import com.barlow.core.domain.registration.ExternalPrincipal;
import com.barlow.core.domain.registration.ProviderAndSubQuery;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class UserReader {

	private final UserRepository userRepository;

	public UserReader(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User read(long userNo) {
		return userRepository.retrieve(new UserQuery(userNo));
	}

	public User read(ExternalPrincipal principal) {
		return userRepository.findByProviderAndSub(new ProviderAndSubQuery(principal.authProvider(), principal.sub()));
	}
}
