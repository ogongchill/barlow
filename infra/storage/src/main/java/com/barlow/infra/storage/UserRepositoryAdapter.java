package com.barlow.infra.storage;

import com.barlow.core.domain.account.AccountDomainException;
import com.barlow.core.domain.account.create.GuestToMemberCommand;
import com.barlow.core.domain.account.authprovider.ProviderAndSubQuery;
import com.barlow.core.domain.account.myinfo.AccountProfile;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.create.UserRegisterCommand;
import com.barlow.core.domain.account.UserRepository;

@Component
public class UserRepositoryAdapter implements UserRepository {

	private final UserRepositoryJpaRepository userRepositoryJpaRepository;

	public UserRepositoryAdapter(UserRepositoryJpaRepository userRepositoryJpaRepository) {
		this.userRepositoryJpaRepository = userRepositoryJpaRepository;
	}

	@Override
	public User retrieve(UserQuery query) {
		return userRepositoryJpaRepository.findByNo(query.userNo()).toUser();
	}

	@Override
	public AccountProfile retrieveProfile(UserQuery query) {
		return userRepositoryJpaRepository.findByNo(query.userNo()).toAccountProfile();
	}

	@Override
	public User create(UserRegisterCommand command) {
		return userRepositoryJpaRepository.save(UserJpaEntity.fromCommand(command)).toUser();
	}

	@Override
	public User promoteToMember(GuestToMemberCommand command) {
		int result = userRepositoryJpaRepository.changeRole(command.userNo(), User.Role.MEMBER);
		if (result == 0) {
			throw AccountDomainException.modificationException(command.userNo() + "를 MEMBER로 변경하지 못했습니다.");
		}
		return userRepositoryJpaRepository.findByNo(command.userNo()).toUser();
	}

	@Override
	public User findByProviderAndSub(ProviderAndSubQuery query) {
		UserJpaEntity userJpaEntity = userRepositoryJpaRepository
			.findByProviderAndSub(query.authProvider(), query.sub());
		if (userJpaEntity == null) {
			throw AccountDomainException.accountNotFound();
		}
		return userJpaEntity.toUser();
	}

	@Override
	public void delete(User user) {
		userRepositoryJpaRepository.deleteByUserNo(user.getUserNo());
	}
}
