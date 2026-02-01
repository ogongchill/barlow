package com.barlow.core.storage;

import com.barlow.core.domain.registration.Term;
import com.barlow.core.domain.registration.TermAgreement;
import com.barlow.core.domain.registration.TermRepository;
import com.barlow.core.domain.registration.UserTermAgreementCommand;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermRepositoryAdapter implements TermRepository {

    private final TermJpaRepository termJpaRepository;
    private final TermAgreementJpaRepository termAgreementJpaRepository;

    public TermRepositoryAdapter(TermJpaRepository termJpaRepository, TermAgreementJpaRepository termAgreementJpaRepository) {
        this.termJpaRepository = termJpaRepository;
        this.termAgreementJpaRepository = termAgreementJpaRepository;
    }

    @Override
    public List<Term> retrieveActiveTerms() {
        return termJpaRepository.findAllByActiveTerms()
                .stream()
                .map(TermJpaEntity::toTerm)
                .toList();
    }

    @Override
    public List<TermAgreement> saveUserAgreement(UserTermAgreementCommand termAgreementCommand) {
        List<TermAgreementJpaEntity> entities = TermAgreementJpaEntity.fromCommand(termAgreementCommand);
        return termAgreementJpaRepository.saveAll(entities)
                .stream()
                .map(TermAgreementJpaEntity::toTermAgreement)
                .toList();
    }

    @Override
    public void deleteByUserNo(long userNo) {
        termAgreementJpaRepository.deleteAllByMemberNo(userNo);
    }
}
