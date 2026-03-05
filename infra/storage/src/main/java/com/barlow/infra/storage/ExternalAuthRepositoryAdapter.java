package com.barlow.infra.storage;

import com.barlow.core.domain.account.RegistrationException;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.externalauth.ExternalAuthRepository;
import com.barlow.core.domain.externalauth.ExternalPrincipal;
import com.barlow.core.domain.externalauth.UserExternalAuth;
import com.barlow.core.domain.externalauth.ExternalAuthCreateCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExternalAuthRepositoryAdapter implements ExternalAuthRepository {

	private final ExternalAuthJpaRepository authProviderJpaRepository;

	public ExternalAuthRepositoryAdapter(ExternalAuthJpaRepository authProviderJpaRepository) {
		this.authProviderJpaRepository = authProviderJpaRepository;
	}

	@Override
	public UserExternalAuth create(ExternalAuthCreateCommand command) {
		try {
			authProviderJpaRepository.save(ExternalAuthJpaEntity.fromCommand(command));
			return retrieveByUser(new UserQuery(command.userNo()));
		} catch (DataIntegrityViolationException e) { // DB 제약을 도메인 예외로 변환합니다.
			throw RegistrationException.oauthAlreadyRegistered();
		}
	}

	@Override
	public UserExternalAuth retrieveByUser(UserQuery userQuery) {
		List<ExternalAuthJpaEntity> entities = authProviderJpaRepository.findAllByMemberNo(userQuery.userNo());
		List<ExternalPrincipal> principals = entities.stream().map(ExternalAuthJpaEntity::toExternalPrincipal).toList();
		return new UserExternalAuth(userQuery.userNo(), principals);
	}

	@Override
	public boolean existsByProviderAndSub(ExternalAuthCreateCommand command) {
		return authProviderJpaRepository.existsByProviderAndSub(command.authProvider(), command.sub());
	}

	@Override
	public void deleteByUserNo(long userNo) {
		authProviderJpaRepository.deleteAllByMemberNo(userNo);
	}
}
