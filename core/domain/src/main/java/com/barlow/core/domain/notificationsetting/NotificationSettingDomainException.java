package com.barlow.core.domain.notificationsetting;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.exception.CoreDomainExceptionCode;
import com.barlow.core.exception.CoreDomainExceptionLevel;
import com.barlow.core.exception.CoreDomainExceptionType;

public class NotificationSettingDomainException extends CoreDomainException {

	public NotificationSettingDomainException(CoreDomainExceptionCode code, CoreDomainExceptionLevel level, String message) {
		super(code, level, message);
	}

	public NotificationSettingDomainException(CoreDomainExceptionType exceptionType, String message) {
		super(exceptionType, message);
	}

	public static NotificationSettingDomainException alreadyRegistered(String committeeName) {
		return new NotificationSettingDomainException(
			CoreDomainExceptionType.CONFLICT_EXCEPTION,
			String.format("이미 알림 설정 되어있는 %s 입니다", committeeName)
		);
	}
}
