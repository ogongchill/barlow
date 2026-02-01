package com.barlow.core.domain.account;

import com.barlow.core.domain.account.create.GuestToMemberCommand;
import com.barlow.core.domain.account.authprovider.ProviderAndSubQuery;
import com.barlow.core.domain.account.create.UserRegisterCommand;
import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface UserRepository {

	User retrieve(UserQuery query);

	User create(UserRegisterCommand command);

	User promoteToMember(GuestToMemberCommand command);

	User findByProviderAndSub(ProviderAndSubQuery query);

	void delete(User user);
}
