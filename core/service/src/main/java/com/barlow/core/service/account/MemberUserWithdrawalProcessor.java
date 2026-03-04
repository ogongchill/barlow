package com.barlow.core.service.account;

import com.barlow.core.domain.account.UserRepository;
import com.barlow.core.domain.account.device.DeviceRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.Passport;
import com.barlow.core.domain.User;
import com.barlow.core.domain.account.withdrawal.UserWithdrawalProcessor;
import com.barlow.core.service.legislationaccount.impl.LegislationAccountWithdrawalHandler;
import com.barlow.core.service.notificationsetting.impl.NotificationWithdrawalHandler;
import com.barlow.core.domain.account.authprovider.AuthProviderRepository;
import com.barlow.core.domain.account.term.TermRepository;
import com.barlow.core.service.subscribe.SubscribeWithdrawalHandler;

@Component
class MemberUserWithdrawalProcessor implements UserWithdrawalProcessor {

	private final UserRepository userRepository;
	private final DeviceRepository deviceRepository;
	private final AuthProviderRepository authProviderRepository;
	private final TermRepository termRepository;

	private final NotificationWithdrawalHandler notificationWithdrawalHandler;
	private final SubscribeWithdrawalHandler subscribeWithdrawalHandler;
	private final LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler;

	public MemberUserWithdrawalProcessor(
		UserRepository userRepository,
		DeviceRepository deviceRepository,
		AuthProviderRepository authProviderRepository,
		TermRepository termRepository,
		NotificationWithdrawalHandler notificationWithdrawalHandler,
		SubscribeWithdrawalHandler subscribeWithdrawalHandler,
		LegislationAccountWithdrawalHandler legislationAccountWithdrawalHandler
	) {
		this.userRepository = userRepository;
		this.deviceRepository = deviceRepository;
		this.authProviderRepository = authProviderRepository;
		this.termRepository = termRepository;
		this.notificationWithdrawalHandler = notificationWithdrawalHandler;
		this.subscribeWithdrawalHandler = subscribeWithdrawalHandler;
		this.legislationAccountWithdrawalHandler = legislationAccountWithdrawalHandler;
	}

	@Override
	@Transactional
	public void process(Passport passport) {
		User user = passport.getUser();
		long userNo = user.getUserNo();

		// Member 전용 데이터 삭제
		authProviderRepository.deleteByUserNo(userNo);
		termRepository.deleteByUserNo(userNo);

		// 공통 데이터 삭제
		userRepository.delete(user);
		deviceRepository.deleteById(passport.getDeviceId());

		// 비관심사
		notificationWithdrawalHandler.handle(user);
		subscribeWithdrawalHandler.handle(user);
		legislationAccountWithdrawalHandler.handle(user);
	}

	@Override
	public User.Role supportedRole() {
		return User.Role.MEMBER;
	}
}
