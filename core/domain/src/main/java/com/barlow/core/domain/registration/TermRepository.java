package com.barlow.core.domain.registration;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermRepository {

    List<Term> retrieveActiveTerms();

    List<TermAgreement> saveUserAgreement(UserTermAgreementCommand termAgreementCommand);

    void deleteByUserNo(long userNo);
}
