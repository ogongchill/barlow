package com.barlow.core.service.business.account.withdrawal;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.Passport;
import com.barlow.core.service.implement.account.withdrawal.UserWithdrawalOrchestrator;

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
