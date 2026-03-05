package com.barlow.core.domain.account;

import com.barlow.core.domain.externalauth.ExternalSubQuery;


import com.barlow.core.domain.User;

public interface UserRepository {

	User retrieve(UserQuery query);

	AccountProfile retrieveProfile(UserQuery query);

	User create(UserRegisterCommand command);

	User promoteToMember(GuestToMemberCommand command);

	User findByProviderAndSub(ExternalSubQuery query);

	void delete(User user);
}
