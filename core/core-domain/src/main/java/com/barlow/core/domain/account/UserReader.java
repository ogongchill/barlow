package com.barlow.core.domain.account;

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
}
