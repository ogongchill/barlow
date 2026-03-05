package com.barlow.core.service.account.business;

import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.device.DeviceRepository;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserWithdrawalProcessor;
import com.barlow.core.service.legislationaccount.impl.LegislationAccountWithdrawalHandler;
import com.barlow.core.service.notificationsetting.impl.NotificationWithdrawalHandler;
import com.barlow.core.service.subscribe.SubscriptionWithdrawalHandler;

@Component
class GuestUserWithdrawalProcessor implements UserWithdrawalProcessor {

	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;

	private final NotificationWithdrawalHandler notificationWithdrawalHandler;
	private final SubscriptionWithdrawalHandler subscribeWithdrawalHandler;
	private final LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler;

	public GuestUserWithdrawalProcessor(UserRepository userRepository, DeviceRepository deviceRepository,
		NotificationWithdrawalHandler notificationWithdrawalHandler,
		SubscriptionWithdrawalHandler subscribeWithdrawalHandler,
		LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler) {
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
		this.notificationWithdrawalHandler = notificationWithdrawalHandler;
		this.subscribeWithdrawalHandler = subscribeWithdrawalHandler;
		this.legislationAccountWithdrawalHandler = legislationAccountWithdrawalHandler;
	}

	@Override
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

	@Override
	public User.Role supportedRole() {
		return User.Role.GUEST;
	}
}
