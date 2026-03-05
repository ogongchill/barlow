package com.barlow.core.domain.account.term;

import java.util.List;

public interface TermRepository {

	List<Term> retrieveActiveTerms();

	List<TermAgreement> saveUserAgreement(UserTermAgreementCommand termAgreementCommand);

	void deleteByUserNo(long userNo);
}
