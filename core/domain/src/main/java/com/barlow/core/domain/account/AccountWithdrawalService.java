package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;

@Service
public class AccountWithdrawalService {

	private final UserWithdrawalFactory userWithdrawalFactory;

	AccountWithdrawalService(UserWithdrawalFactory userWithdrawalFactory) {
		this.userWithdrawalFactory = userWithdrawalFactory;
	}

	public void withdraw(Passport passport) {
		UserWithdrawalProcessor userWithdrawalProcessor = userWithdrawalFactory.getBy(passport.getUser());
		userWithdrawalProcessor.process(passport);
	}
}
