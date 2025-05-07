package com.barlow.core.storage;

import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.MyLegislationAccount;
import com.barlow.core.domain.home.MyLegislationAccountRepository;

@Component
public class MyLegislationAccountRepositoryAdapter implements MyLegislationAccountRepository {

	private final LegislationAccountJpaRepository legislationAccountJpaRepository;

	public MyLegislationAccountRepositoryAdapter(LegislationAccountJpaRepository legislationAccountJpaRepository) {
		this.legislationAccountJpaRepository = legislationAccountJpaRepository;
	}

	@Override
	public List<MyLegislationAccount> retrieveMyLegislationAccounts(User user) {
		return legislationAccountJpaRepository.findSubscribedLegislationAccountsByMemberNo(user.getUserNo())
			.stream()
			.map(LegislationAccountJpaEntity::toMyLegislationAccount)
			.toList();
	}
}
