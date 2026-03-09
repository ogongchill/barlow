package com.barlow.core.service.home.impl;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.MyLegislationAccountRepository;
import com.barlow.core.domain.notificationcenter.MyNotificationCenterRepository;
import com.barlow.core.service.home.MyHomeStatus;

@Component
public class MyHomeInfoReader {

	private final MyLegislationAccountRepository myLegislationAccountRepository;
	private final MyNotificationCenterRepository myNotificationCenterRepository;

	public MyHomeInfoReader(MyLegislationAccountRepository myLegislationAccountRepository,
		MyNotificationCenterRepository myNotificationCenterRepository) {
		this.myLegislationAccountRepository = myLegislationAccountRepository;
		this.myNotificationCenterRepository = myNotificationCenterRepository;
	}

	public MyHomeStatus readHome(User user) {
		return new MyHomeStatus(
			myLegislationAccountRepository.retrieveMyLegislationAccounts(user),
			myNotificationCenterRepository.existsTodayNotification(user));
	}
}
