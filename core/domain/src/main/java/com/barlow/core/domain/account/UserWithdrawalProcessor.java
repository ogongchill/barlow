package com.barlow.core.domain.account;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;

interface UserWithdrawalProcessor {

	void process(Passport passport);

	User.Role supportedRole();
}
