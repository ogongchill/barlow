package com.barlow.core.domain.account;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.LegislationAccountWithdrawalHandler;
import com.barlow.core.domain.notificationsetting.NotificationWithdrawalHandler;
import com.barlow.core.domain.subscribe.SubscribeWithdrawalHandler;

@Component
class GuestUserWithdrawalProcessor implements UserWithdrawalProcessor {

	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;

	private final NotificationWithdrawalHandler notificationWithdrawalHandler;
	private final SubscribeWithdrawalHandler subscribeWithdrawalHandler;
	private final LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler;

	public GuestUserWithdrawalProcessor(
		UserRepository userRepository, DeviceRepository deviceRepository,
		NotificationWithdrawalHandler notificationWithdrawalHandler,
		SubscribeWithdrawalHandler subscribeWithdrawalHandler,
		LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler
	) {
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
		this.notificationWithdrawalHandler = notificationWithdrawalHandler;
		this.subscribeWithdrawalHandler = subscribeWithdrawalHandler;
		this.legislationAccountWithdrawalHandler = legislationAccountWithdrawalHandler;
	}

	@Override
	@Transactional
	public void process(Passport passport) {
		User user = passport.getUser();
		// 주요 관심사
		userRepository.delete(user);
		deviceRepository.deleteById(passport.getDeviceId());

		// 비관심사
		notificationWithdrawalHandler.handle(user);
		subscribeWithdrawalHandler.handle(user);
		legislationAccountWithdrawalHandler.handle(user);
	}
}
