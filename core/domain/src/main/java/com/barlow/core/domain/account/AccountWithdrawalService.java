package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;

@Service
public class AccountWithdrawalService {

	private final UserWithdrawalOrchestrator userWithdrawalOrchestrator;

	AccountWithdrawalService(UserWithdrawalOrchestrator userWithdrawalOrchestrator) {
		this.userWithdrawalOrchestrator = userWithdrawalOrchestrator;
	}

	public void withdraw(Passport passport) {
		userWithdrawalOrchestrator.process(passport);
	}
}
