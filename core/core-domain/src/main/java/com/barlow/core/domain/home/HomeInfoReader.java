package com.barlow.core.domain.home;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
public class HomeInfoReader {

	private final MyLegislationAccountRepository myLegislationAccountRepository;
	private final MyNotificationCenterRepository myNotificationCenterRepository;

	public HomeInfoReader(
		MyLegislationAccountRepository myLegislationAccountRepository,
		MyNotificationCenterRepository myNotificationCenterRepository
	) {
		this.myLegislationAccountRepository = myLegislationAccountRepository;
		this.myNotificationCenterRepository = myNotificationCenterRepository;
	}

	public HomeStatus readHome(User user) {
		return new HomeStatus(
			myLegislationAccountRepository.retrieveMyLegislationAccounts(user),
			myNotificationCenterRepository.existsTodayNotification(user)
		);
	}
}
