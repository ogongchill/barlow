package com.barlow.core.domain.account;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;

@Component
class UserWithdrawalOrchestrator {

	private final Map<User.Role, UserWithdrawalProcessor> processors;

	UserWithdrawalOrchestrator(List<UserWithdrawalProcessor> processors) {
		this.processors = processors.stream()
			.collect(Collectors.toMap(UserWithdrawalProcessor::supportedRole, processor -> processor));
	}

	void process(Passport passport) {
		processors.get(passport.getUser().getRole())
			.process(passport);
	}
}
