package com.barlow.storage.db.core;

import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LegislationRepositoryAdapter implements LegislationAccountRepository {

    private final LegislationAccountJpaRepository legislationAccountJpaRepository;

    public LegislationRepositoryAdapter(LegislationAccountJpaRepository legislationAccountJpaRepository) {
        this.legislationAccountJpaRepository = legislationAccountJpaRepository;
    }

    @Override
    public List<LegislationAccount> retrieveCommitteeAccount() {
        return legislationAccountJpaRepository.findAll()
                .stream()
                .filter(LegislationAccountJpaEntity::isCommittee)
                .map(LegislationAccountJpaEntity::toLegislationAccount)
                .toList();
    }
}
