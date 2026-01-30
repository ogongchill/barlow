package com.barlow.core.domain.account;

import com.barlow.core.domain.registration.GuestToMemberCommand;
import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface UserRepository {

	User retrieve(UserQuery query);

	User create(UserRegisterCommand command);

	User promoteToMember(GuestToMemberCommand command);

	void delete(User user);
}
