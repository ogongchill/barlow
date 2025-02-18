package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegislationAccountRepository {

	LegislationAccount retrieve(long accountNo);

	List<LegislationAccount> retrieveCommitteeAccount();

	void incrementSubscriber(long accountNo);

	void decrementSubscriber(long accountNo);
}
