package com.barlow.storage.db.core;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRegisterCommand;
import com.barlow.core.domain.account.UserRepository;

@Component
public class UserRepositoryAdapter implements UserRepository {

	private final UserRepositoryJpaRepository userRepositoryJpaRepository;

	public UserRepositoryAdapter(UserRepositoryJpaRepository userRepositoryJpaRepository) {
		this.userRepositoryJpaRepository = userRepositoryJpaRepository;
	}

	@Override
	public User retrieve(UserQuery query) {
		return userRepositoryJpaRepository.findByNo(query.userNo())
			.toUser();
	}

	@Override
	public User create(UserRegisterCommand command) {
		return userRepositoryJpaRepository
			.save(UserJpaEntity.guestOf(command.nickname()))
			.toUser();
	}
}
