package com.barlow.core.domain.account;

import com.barlow.core.domain.Passport;

interface UserWithdrawalProcessor {
	void process(Passport passport);
}
