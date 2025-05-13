package com.barlow.core.domain.account;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;

@Component
class UserWithdrawalFactory {

	private final GuestUserWithdrawalProcessor guestUserWithdrawalProcessor;

	UserWithdrawalFactory(GuestUserWithdrawalProcessor guestUserWithdrawalProcessor) {
		this.guestUserWithdrawalProcessor = guestUserWithdrawalProcessor;
	}

	/**
	 * 현재 시스템에는 GUEST 유저만 존재함 -> if 만 탐
	 * 다음 버전부터 다른 종류의 ROLE 을 가진 유저 생성됨
	 */
	UserWithdrawalProcessor getBy(User user) {
		if (user.isGuestUser()) {
			return guestUserWithdrawalProcessor;
		}
		return null;
	}
}
