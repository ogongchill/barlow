package com.barlow.core.domain.account;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegislationAccountRepository {

	List<LegislationAccount> retrieveCommitteeAccount();
}
