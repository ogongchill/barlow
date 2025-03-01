package com.barlow.storage.db.core;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserCreateCommand;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.UserRepository;

@Component
public class UserRepositoryAdapter implements UserRepository {

	private final UserRepositoryJpaRepository userRepositoryJpaRepository;
	private final DeviceJpaRepository deviceJpaRepository;

	public UserRepositoryAdapter(
		UserRepositoryJpaRepository userRepositoryJpaRepository,
		DeviceJpaRepository deviceJpaRepository
	) {
		this.userRepositoryJpaRepository = userRepositoryJpaRepository;
		this.deviceJpaRepository = deviceJpaRepository;
	}

	@Override
	public User retrieve(UserQuery query) {
		return userRepositoryJpaRepository.findByNo(query.userNo())
			.toUser();
	}

	@Override
	public User create(UserCreateCommand command) {
		UserJpaEntity userJpaEntity = userRepositoryJpaRepository.save(UserJpaEntity.guestOf(command.nickname()));
		deviceJpaRepository.save(new DeviceJpaEntity(
			command.deviceId(),
			command.os(),
			command.deviceToken(),
			userJpaEntity.getNo())
		);
		return userJpaEntity.toUser();
	}
}
