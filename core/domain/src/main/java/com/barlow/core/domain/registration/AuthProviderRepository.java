package com.barlow.core.domain.registration;

import com.barlow.core.domain.account.UserQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthProviderRepository {

    UserAuthProvider create(UserAuthProviderCreateCommand command);

    UserAuthProvider retrieveByUser(UserQuery userQuery);

    boolean existsByProviderAndSub(UserAuthProviderCreateCommand command);

    boolean existsByUserIdAndProvider(UserAuthProviderCreateCommand command);

    void deleteByUserNo(long userNo);
}
