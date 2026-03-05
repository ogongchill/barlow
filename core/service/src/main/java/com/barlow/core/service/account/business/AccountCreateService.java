package com.barlow.core.service.account.business;

import com.barlow.core.domain.account.AccountDomainException;
import com.barlow.core.domain.account.term.TermAgreement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.core.domain.User;
import com.barlow.core.domain.account.UserCreateCommand;
import com.barlow.core.service.account.impl.TermManager;
import com.barlow.core.service.account.impl.UserCreator;
import com.barlow.core.service.notificationsetting.impl.NotificationSettingActivator;

import java.util.List;

@Service
public class AccountCreateService {

	private final UserCreator userCreator;
	private final NotificationSettingActivator notificationSettingActivator;
	private final TermManager termManager;

	public AccountCreateService(UserCreator userCreator, NotificationSettingActivator notificationSettingActivator,
		TermManager termManager) {
		this.userCreator = userCreator;
		this.notificationSettingActivator = notificationSettingActivator;
		this.termManager = termManager;
	}

	@Transactional
	public User createGuest(UserCreateCommand command, List<TermAgreement> agreements) {
		if (command.role() != User.Role.GUEST) {
			throw AccountDomainException.modificationException("Guest User 요청이 아닌 상태로 User를 생성 할 수 없습니다.");
		}
		termManager.validateAgreements(agreements);
		User user = userCreator.create(command);
		termManager.saveAgreements(agreements, user);
		notificationSettingActivator.activateDefault(user);
		return user;
	}
}
