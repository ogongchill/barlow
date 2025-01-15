package com.barlow.storage.db.core;

import static com.barlow.storage.db.core.NotificationTopic.HOUSE_STEERING;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.notification.NotificationCenterItemInfo;
import com.barlow.storage.db.CoreDbContextTest;

@Transactional
class NotificationCenterRepositoryAdapterTest extends CoreDbContextTest {

	private final NotificationCenterRepositoryAdapter notificationCenterRepositoryAdapter;
	private final NotificationCenterJpaRepository notificationCenterJpaRepository;

	NotificationCenterRepositoryAdapterTest(
		NotificationCenterRepositoryAdapter notificationCenterRepositoryAdapter,
		NotificationCenterJpaRepository notificationCenterJpaRepository
	) {
		this.notificationCenterRepositoryAdapter = notificationCenterRepositoryAdapter;
		this.notificationCenterJpaRepository = notificationCenterJpaRepository;
	}

	@DisplayName("알림센터에 저장할 정보들로 NotificationCenter 테이블에 batch insert 하면 정상적으로 저장된다")
	@Test
	void registerAll() {
		notificationCenterRepositoryAdapter.registerAll(List.of(
			new NotificationCenterItemInfo(1L, HOUSE_STEERING.name(), "title", "body"),
			new NotificationCenterItemInfo(2L, HOUSE_STEERING.name(), "title", "body")
		));

		assertThat(notificationCenterJpaRepository.findAll()).hasSize(2);
	}
}