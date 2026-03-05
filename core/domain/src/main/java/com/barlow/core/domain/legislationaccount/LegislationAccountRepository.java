package com.barlow.core.domain.legislationaccount;


import java.util.List;

import com.barlow.core.enumerate.LegislationType;

public interface LegislationAccountRepository {

	LegislationAccount retrieve(LegislationType legislationType);

	List<LegislationAccount> retrieveCommitteeAccount();

	void incrementSubscriber(LegislationType legislationType);

	void decrementSubscriber(LegislationType legislationType);
}
