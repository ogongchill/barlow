package com.barlow.core.domain.legislationaccount;

import java.util.List;

import org.springframework.stereotype.Service;

import com.barlow.core.domain.User;
import com.barlow.core.enumerate.LegislationType;

@Service
public class LegislationAccountRetrieveService {

	private final LegislationAccountReader legislationAccountReader;

	public LegislationAccountRetrieveService(LegislationAccountReader legislationAccountReader) {
		this.legislationAccountReader = legislationAccountReader;
	}

	public LegislationAccount retrieve(LegislationType legislationType, User user) {
		return legislationAccountReader.read(legislationType, user);
	}

	public List<LegislationAccount> retrieveAllCommittees(User user) {
		return legislationAccountReader.readAllCommittees(user);
	}
}
