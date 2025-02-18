package com.barlow.core.domain.legislationaccount;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.domain.notificationsetting.NotificationSettingReader;
import com.barlow.core.domain.subscribe.Subscribe;
import com.barlow.core.domain.subscribe.SubscribeReader;

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

	public LegislationAccount read(long accountNo, User user) {
		LegislationAccount legislationAccount = legislationAccountRepository.retrieve(accountNo);
		legislationAccount.setNotifiable(notificationSettingReader
			.readNotificationSetting(legislationAccount.getLegislationType(), user)
			.isNotifiable());
		legislationAccount.setSubscribed(subscribeReader
			.readSubscribe(accountNo, user)
			.isActive());
		return legislationAccount;
	}

	public List<LegislationAccount> readAllCommittees(User user) {
		List<LegislationAccount> legislationAccounts = legislationAccountRepository.retrieveCommitteeAccount();
		Map<String, Boolean> memberNotificationSetting = notificationSettingReader.readNotificationSettings(user).stream()
			.collect(Collectors.toMap(NotificationSetting::getTopicName, NotificationSetting::isNotifiable));
		Map<Long, Boolean> memberSubscription = subscribeReader.readSubscribes(user).stream()
			.collect(Collectors.toMap(Subscribe::getLegislationAccountNo, Subscribe::isActive));

		legislationAccounts.forEach(legislationAccount -> {
			legislationAccount.setNotifiable(memberNotificationSetting.get(legislationAccount.getLegislationType()));
			legislationAccount.setSubscribed(memberSubscription.get(legislationAccount.getNo()));
		});
		return legislationAccounts;
	}
}
