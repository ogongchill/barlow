package com.barlow.core.domain.home;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.notificationcenter.MyNotificationCenterRepository;

@Component
public class MyHomeInfoReader {

	private final MyLegislationAccountRepository myLegislationAccountRepository;
	private final MyNotificationCenterRepository myNotificationCenterRepository;

	public MyHomeInfoReader(
		MyLegislationAccountRepository myLegislationAccountRepository,
		MyNotificationCenterRepository myNotificationCenterRepository
	) {
		this.myLegislationAccountRepository = myLegislationAccountRepository;
		this.myNotificationCenterRepository = myNotificationCenterRepository;
	}

	public MyHomeStatus readHome(User user) {
		return new MyHomeStatus(
			myLegislationAccountRepository.retrieveMyLegislationAccounts(user),
			myNotificationCenterRepository.existsTodayNotification(user)
		);
	}
}
