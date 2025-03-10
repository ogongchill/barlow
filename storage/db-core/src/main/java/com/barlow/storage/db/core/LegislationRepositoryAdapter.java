package com.barlow.storage.db.core;

import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRepository;
import com.barlow.core.enumerate.LegislationType;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LegislationRepositoryAdapter implements LegislationAccountRepository {

	private final LegislationAccountJpaRepository legislationAccountJpaRepository;

	public LegislationRepositoryAdapter(LegislationAccountJpaRepository legislationAccountJpaRepository) {
		this.legislationAccountJpaRepository = legislationAccountJpaRepository;
	}

	@Override
	public LegislationAccount retrieve(LegislationType legislationType) {
		return legislationAccountJpaRepository.findByNo(legislationType.getLegislationNo())
			.toLegislationAccount();
	}

	@Override
	public List<LegislationAccount> retrieveCommitteeAccount() {
		return legislationAccountJpaRepository.findAll()
			.stream()
			.filter(LegislationAccountJpaEntity::isCommittee)
			.map(LegislationAccountJpaEntity::toLegislationAccount)
			.toList();
	}

	@Override
	public void incrementSubscriber(LegislationType legislationType) {
		legislationAccountJpaRepository.updateIncrementSubscriberCount(legislationType.getLegislationNo());
	}

	@Override
	public void decrementSubscriber(LegislationType legislationType) {
		legislationAccountJpaRepository.updateDecrementSubscriberCount(legislationType.getLegislationNo());
	}
}
