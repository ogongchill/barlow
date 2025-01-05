package com.barlow.storage.db.core;

import com.barlow.core.domain.account.LegislationCommitteeAccount;
import com.barlow.core.domain.account.LegislationCommitteeAccountRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
public class LegislationCommitteeAccountRepositoryAdapter implements LegislationCommitteeAccountRepository {

    private final LegislationAccountJpaRepository legislationAccountJpaRepository;

    public LegislationCommitteeAccountRepositoryAdapter(LegislationAccountJpaRepository legislationAccountJpaRepository) {
        this.legislationAccountJpaRepository = legislationAccountJpaRepository;
    }

    @Override
    public List<LegislationCommitteeAccount> retrieveAll() {
        return legislationAccountJpaRepository.findAll()
                .stream()
                .filter(this::isCommittee)
                .map(this::toLegislationCommitteeAccount)
                .toList();
    }

    @Override
    public List<LegislationCommitteeAccount> retrieveStandingCommitteeAccount() {
        return retrieveAll();
    }

    @Override
    public LegislationCommitteeAccount findByName(String targetName) {
        return legislationAccountJpaRepository.findAll()
                .stream()
                .filter(this::isCommittee)
                .filter(committeeEntity -> committeeEntity.getLegislationType().getValue().equals(targetName))
                .map(this::toLegislationCommitteeAccount)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public LegislationCommitteeAccount findByCommitteeCode(String targetCode) {
        Long targetCodeLong = Long.parseLong(targetCode);
        return legislationAccountJpaRepository.findAll()
                .stream()
                .filter(this::isCommittee)
                .filter(committeeEntity -> committeeEntity.getNo().equals(targetCodeLong))
                .map(this::toLegislationCommitteeAccount)
                .findFirst()
                .orElseThrow(NoSuchElementException::new);
    }

    private boolean isCommittee(LegislationAccountJpaEntity account) {
        return !account.getLegislationType().equals(LegislationType.GOVERNMENT)
               && !account.getLegislationType().equals(LegislationType.SPEAKER);
    }

    private LegislationCommitteeAccount toLegislationCommitteeAccount(LegislationAccountJpaEntity legislationAccountJpaEntity) {
        return LegislationCommitteeAccount.builder()
                .name(legislationAccountJpaEntity.getLegislationType().name())
                .accountNo(legislationAccountJpaEntity.getNo())
                .iconUrl(legislationAccountJpaEntity.getLegislationType().getIconUrl())
                .description(legislationAccountJpaEntity.getDescription())
                .postCount(legislationAccountJpaEntity.getPostCount())
                .subscriberCount(legislationAccountJpaEntity.getSubscriberCount())
                .build();
    }
}
