package com.barlow.core.storage.notification;

import static com.barlow.core.enumerate.NotificationTopic.HOUSE_STEERING;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.notification.NotificationCenterItemInfo;
import com.barlow.core.storage.CoreDbContextTest;
import com.barlow.storage.db.core.NotificationCenterJpaRepository;
import com.barlow.storage.db.core.notification.NotificationCenterRepositoryAdapter;

class NotificationCenterRepositoryAdapterTest extends CoreDbContextTest {

	private final NotificationCenterRepositoryAdapter adapter;
	private final NotificationCenterJpaRepository notificationCenterJpaRepository;

	NotificationCenterRepositoryAdapterTest(
		NotificationCenterRepositoryAdapter adapter,
		NotificationCenterJpaRepository notificationCenterJpaRepository
	) {
		this.adapter = adapter;
		this.notificationCenterJpaRepository = notificationCenterJpaRepository;
	}

	@DisplayName("알림센터에 저장할 정보들로 NotificationCenter 테이블에 batch insert 하면 정상적으로 저장된다")
	@Test
	@Transactional
	void registerAll() {
		adapter.registerAll(List.of(
			new NotificationCenterItemInfo(
				1L, HOUSE_STEERING,
				List.of(new NotificationCenterItemInfo.BillItemInfo("billId1", "billName1"))
			),
			new NotificationCenterItemInfo(
				2L, HOUSE_STEERING,
				List.of(new NotificationCenterItemInfo.BillItemInfo("billId2", "billName2"))
			)
		));

		assertThat(notificationCenterJpaRepository.findAll()).hasSize(2);
	}
}