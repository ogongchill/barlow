package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Repository;

import java.util.List;

import com.barlow.core.enumerate.LegislationType;

@Repository
public interface LegislationAccountRepository {

	LegislationAccount retrieve(LegislationType legislationType);

	List<LegislationAccount> retrieveCommitteeAccount();

	void incrementSubscriber(LegislationType legislationType);

	void decrementSubscriber(LegislationType legislationType);
}
