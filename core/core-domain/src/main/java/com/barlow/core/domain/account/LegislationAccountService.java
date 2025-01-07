package com.barlow.core.domain.account;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegislationAccountService {

    private final LegislationAccountRepository legislationAccountInfoRepository;

    public LegislationAccountService(LegislationAccountRepository legislationAccountInfoRepository) {
        this.legislationAccountInfoRepository = legislationAccountInfoRepository;
    }

    public List<LegislationAccount> retrieveCommitteeAccountInfo() {
        return legislationAccountInfoRepository.retrieveCommitteeAccount();
    }
}
