package com.barlow.infra.storage;

import com.barlow.core.domain.account.RegistrationException;
import com.barlow.core.domain.account.UserQuery;
import com.barlow.core.domain.account.authprovider.AuthProviderRepository;
import com.barlow.core.domain.account.authprovider.ExternalPrincipal;
import com.barlow.core.domain.account.authprovider.UserAuthProvider;
import com.barlow.core.domain.account.authprovider.UserAuthProviderCreateCommand;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthProviderRepositoryAdapter implements AuthProviderRepository {

    private final AuthProviderJpaRepository authProviderJpaRepository;

    public AuthProviderRepositoryAdapter(AuthProviderJpaRepository authProviderJpaRepository) {
        this.authProviderJpaRepository = authProviderJpaRepository;
    }

    @Override
    public UserAuthProvider create(UserAuthProviderCreateCommand command) {
        try {
            authProviderJpaRepository.save(AuthProviderJpaEntity.fromCommand(command));
            return retrieveByUser(new UserQuery(command.userNo()));
        } catch (DataIntegrityViolationException e) { // DB 제약을 도메인 예외로 변환합니다.
            throw RegistrationException.oauthAlreadyRegistered();
        }
    }

    @Override
    public UserAuthProvider retrieveByUser(UserQuery userQuery) {
        List<AuthProviderJpaEntity> entities = authProviderJpaRepository.findAllByMemberNo(userQuery.userNo());
        List<ExternalPrincipal> principals = entities.stream()
                .map(AuthProviderJpaEntity::toExternalPrincipal)
                .toList();
        return new UserAuthProvider(userQuery.userNo(), principals);
    }

    @Override
    public boolean existsByProviderAndSub(UserAuthProviderCreateCommand command) {
        return authProviderJpaRepository.existsByProviderAndSub(command.authProvider(), command.sub());
    }

    @Override
    public boolean existsByUserIdAndProvider(UserAuthProviderCreateCommand command) {
        return authProviderJpaRepository.existsByMemberNoAndProvider(command.userNo(), command.authProvider());
    }

    @Override
    public void deleteByUserNo(long userNo) {
        authProviderJpaRepository.deleteAllByMemberNo(userNo);
    }
}
