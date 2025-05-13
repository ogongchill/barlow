package com.barlow.core.storage;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationCenterItemRepository;

@Component
public class NotificationCenterItemRepositoryAdapter implements NotificationCenterItemRepository {

	private final NotificationCenterJpaRepository notificationCenterJpaRepository;

	public NotificationCenterItemRepositoryAdapter(NotificationCenterJpaRepository notificationCenterJpaRepository) {
		this.notificationCenterJpaRepository = notificationCenterJpaRepository;
	}

	@Override
	public void deleteAllItems(User user) {
		notificationCenterJpaRepository.deleteAllByMemberNo(user.getUserNo());
	}
}
