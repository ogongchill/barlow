package com.barlow.core.domain.legislationaccount;

import java.util.List;


import com.barlow.core.domain.User;

public interface MyLegislationAccountRepository {
	List<MyLegislationAccount> retrieveMyLegislationAccounts(User user);
}
