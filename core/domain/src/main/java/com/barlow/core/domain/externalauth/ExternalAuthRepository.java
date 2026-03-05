package com.barlow.core.domain.externalauth;

import com.barlow.core.domain.account.UserQuery;

public interface ExternalAuthRepository {

	UserExternalAuth create(ExternalAuthCreateCommand command);

	UserExternalAuth retrieveByUser(UserQuery userQuery);

	boolean existsByProviderAndSub(ExternalAuthCreateCommand command);

	void deleteByUserNo(long userNo);
}
