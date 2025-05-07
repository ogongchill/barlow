package com.barlow.core.domain.home;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.barlow.core.domain.User;

@Repository
public interface MyLegislationAccountRepository {
	List<MyLegislationAccount> retrieveMyLegislationAccounts(User user);
}
