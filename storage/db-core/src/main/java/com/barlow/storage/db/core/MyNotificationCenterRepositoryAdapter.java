package com.barlow.storage.db.core;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.home.MyNotificationCenterRepository;
import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;

@Component
public class MyNotificationCenterRepositoryAdapter implements MyNotificationCenterRepository {

	private final NotificationCenterJpaRepository notificationCenterJpaRepository;

	public MyNotificationCenterRepositoryAdapter(NotificationCenterJpaRepository notificationCenterJpaRepository) {
		this.notificationCenterJpaRepository = notificationCenterJpaRepository;
	}

	@Override
	public boolean existsTodayNotification(User user) {
		LocalDate todayDate = LocalDate.now();
		return notificationCenterJpaRepository.existsByCreatedAtBetweenAndMemberNo(
			todayDate.atStartOfDay(),
			todayDate.plusDays(1).atStartOfDay(),
			user.getUserNo()
		);
	}

	@Override
	public List<NotificationCenterItem> retrieveNotificationItems(User user) {
		return notificationCenterJpaRepository.findByMemberNo(user.getUserNo())
			.stream()
			.map(NotificationCenterJpaEntity::toNotificationItem)
			.toList();
	}
}
