package com.barlow.core.domain.legislationaccount;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegislationAccountRepository {

	List<LegislationAccount> retrieveCommitteeAccount();
}
