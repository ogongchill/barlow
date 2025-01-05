package com.barlow.core.domain.account;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LegislationCommitteeAccountRepository {

    List<LegislationCommitteeAccount> retrieveAll();
    List<LegislationCommitteeAccount> retrieveStandingCommitteeAccount();
    LegislationCommitteeAccount findByName(String accountName);
    LegislationCommitteeAccount findByCommitteeCode(String accountName);
}
