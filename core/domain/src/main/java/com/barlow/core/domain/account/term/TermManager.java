package com.barlow.core.domain.account.term;

import com.barlow.core.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TermManager {

    private final TermRepository termRepository;

    public TermManager(TermRepository termRepository) {
        this.termRepository = termRepository;
    }

    public void validateAgreements(List<TermAgreement> agreements) {
        TermsPolicy policy = TermsPolicy.from(termRepository.retrieveActiveTerms());
        policy.validate(agreements);
    }

    public List<TermAgreement> saveAgreements(List<TermAgreement> agreements, User user) {
        return termRepository.saveUserAgreement(new UserTermAgreementCommand(user.getUserNo(), agreements));
    }

    public List<Term> retrieveActiveTerms() {
        return termRepository.retrieveActiveTerms();
    }
}
