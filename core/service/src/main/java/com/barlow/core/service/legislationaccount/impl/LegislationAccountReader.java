package com.barlow.core.service.legislationaccount.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccount;
import com.barlow.core.domain.legislationaccount.LegislationAccountRepository;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.service.notificationsetting.impl.NotificationSettingReader;
import com.barlow.core.service.subscribe.SubscribeReader;
import com.barlow.core.enumerate.LegislationType;

@Component
public class LegislationAccountReader {

	private final LegislationAccountRepository legislationAccountRepository;
	private final NotificationSettingReader notificationSettingReader;
	private final SubscribeReader subscribeReader;

	public LegislationAccountReader(
		LegislationAccountRepository legislationAccountRepository,
		NotificationSettingReader notificationSettingReader,
		SubscribeReader subscribeReader
	) {
		this.legislationAccountRepository = legislationAccountRepository;
		this.notificationSettingReader = notificationSettingReader;
		this.subscribeReader = subscribeReader;
	}

	public LegislationAccount read(LegislationType legislationType, User user) {
		LegislationAccount legislationAccount = legislationAccountRepository.retrieve(legislationType);
		legislationAccount = legislationAccount.withNotifiable(notificationSettingReader
			.readNotificationSetting(legislationAccount.getType(), user)
			.isNotifiable());
		legislationAccount = legislationAccount.withSubscribed(subscribeReader
			.readSubscribe(legislationType, user)
			.isActive());
		return legislationAccount;
	}

	public List<LegislationAccount> readAllCommittees(User user) {
		List<LegislationAccount> legislationAccounts = legislationAccountRepository.retrieveCommitteeAccount();

		Map<String, Boolean> memberNotificationSetting = notificationSettingReader.readNotificationSettings(user).stream()
			.collect(Collectors.toMap(NotificationSetting::getTopicName, NotificationSetting::isNotifiable));
		Map<String, Boolean> memberSubscription = subscribeReader.readSubscribes(user).stream()
			.collect(Collectors.toMap(Subscribe::getLegislationAccountType, Subscribe::isActive));

		return legislationAccounts.stream()
			.map(account -> account.withNotifiable(memberNotificationSetting.get(account.getLegislationType())))
			.map(account -> account.withSubscribed(memberSubscription.get(account.getLegislationType())))
			.collect(Collectors.toList());
	}
}
