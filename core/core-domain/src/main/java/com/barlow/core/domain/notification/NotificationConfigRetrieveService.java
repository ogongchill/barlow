package com.barlow.core.domain.notification;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationConfigRetrieveService {

	private final NotificationConfigRepository notificationConfigRepository;

	public NotificationConfigRetrieveService(NotificationConfigRepository notificationConfigRepository) {
		this.notificationConfigRepository = notificationConfigRepository;
	}

	public MemberNotificationConfig retrieveMemberNotificationConfig(Long memberNo) {
		List<NotificationConfig> notificationConfig = notificationConfigRepository.retrieveByMemberNo(memberNo);
		return new MemberNotificationConfig(notificationConfig);
	}
}
