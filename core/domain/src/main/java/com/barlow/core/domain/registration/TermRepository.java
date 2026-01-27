package com.barlow.core.domain.registration;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository {

    List<Term> retrieveRequirements();

    List<TermAgreement> saveUserAgreement(UserTermAgreementCommand termAgreementCommand);
}
