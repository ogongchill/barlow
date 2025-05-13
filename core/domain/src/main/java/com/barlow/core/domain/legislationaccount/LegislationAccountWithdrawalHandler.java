package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class LegislationAccountWithdrawalHandler {

	private final LegislationAccountRepository legislationAccountRepository;
	private final LegislationAccountSubscriptionManager legislationAccountSubscriptionManager;

	public LegislationAccountWithdrawalHandler(
		LegislationAccountRepository legislationAccountRepository,
		LegislationAccountSubscriptionManager legislationAccountSubscriptionManager
	) {
		this.legislationAccountRepository = legislationAccountRepository;
		this.legislationAccountSubscriptionManager = legislationAccountSubscriptionManager;
	}

	public void handle(User user) {
		legislationAccountRepository.retrieveCommitteeAccount()
			.forEach(account -> legislationAccountSubscriptionManager.unsubscribe(account.getType(), user));
	}
}
