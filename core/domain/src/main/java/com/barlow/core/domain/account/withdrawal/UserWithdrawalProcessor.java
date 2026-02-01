package com.barlow.core.domain.account.withdrawal;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;

interface UserWithdrawalProcessor {

	void process(Passport passport);

	User.Role supportedRole();
}
