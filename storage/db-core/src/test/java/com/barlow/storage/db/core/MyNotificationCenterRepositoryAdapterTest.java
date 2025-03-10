package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.domain.User;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

@StorageTest("dummy/notificationCenter.json")
class MyNotificationCenterRepositoryAdapterTest extends CoreDbContextTest {

	private final MyNotificationCenterRepositoryAdapter adapter;

	public MyNotificationCenterRepositoryAdapterTest(MyNotificationCenterRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("회원의 알림센터에 있는 알림 정보들을 조회한다")
	@Test
	void retrieveNotificationItems() {
		User user = User.of(1L, User.Role.GUEST);

		assertThat(adapter.retrieveNotificationItems(user))
			.isNotEmpty()
			.hasSize(2);
	}
}