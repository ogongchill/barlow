package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

@Service
public class LegislationAccountService {

    private final LegislationAccountRepository legislationAccountInfoRepository;

    public LegislationAccountService(LegislationAccountRepository legislationAccountRepository) {
        this.legislationAccountInfoRepository = legislationAccountRepository;
    }

    public LegislationAccounts retrieveCommitteeAccount() {
        return new LegislationAccounts(legislationAccountInfoRepository.retrieveCommitteeAccount());
    }
}
