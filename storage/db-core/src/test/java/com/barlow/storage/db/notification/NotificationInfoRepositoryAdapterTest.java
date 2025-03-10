package com.barlow.storage.db.notification;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.notification.NotificationInfo;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.core.NotificationInfoRepositoryAdapter;
import com.barlow.storage.db.support.StorageTest;

@StorageTest({"dummy/notificationSetting.json", "dummy/device.json"})
class NotificationInfoRepositoryAdapterTest extends CoreDbContextTest {

	private final NotificationInfoRepositoryAdapter adapter;

	public NotificationInfoRepositoryAdapterTest(NotificationInfoRepositoryAdapter adapter) {
		this.adapter = adapter;
	}

	@DisplayName("회원이 두 개 이상의 디바이스를 가지고 있어도, ACTIVE 상태 디바이스에 대해서만 알림 정보를 조회한다")
	@Test
	void retrieveNotificationInfosByTopic1() {
		NotificationTopic houseSteering = NotificationTopic.HOUSE_STEERING;
		NotificationInfo notificationInfo = adapter.retrieveNotificationInfosByTopic(houseSteering.getValue());

		assertThat(notificationInfo).isNotNull();
	}

	@DisplayName("특정 알림 주제에 대한 푸시알림이 false 인 알림 정보는 조회하지 않는다")
	@Test
	void retrieveNotificationInfosByTopic2() {
		NotificationTopic topic = NotificationTopic.NATIONAL_POLICY;
		NotificationInfo info = adapter.retrieveNotificationInfosByTopic(topic.getValue());

		assertAll(
			() -> assertThat(info).isNotNull(),
			() -> assertThat(info.getInfos()).isEmpty()
		);
	}

	@DisplayName("NotificationTopic 들로 알림 정보를 조회하면 해당 토픽을 구독한 회원들의 알림설정 정보를 조회한다")
	@Test
	void retrieveNotificationInfosByTopics() {
		NotificationTopic houseSteering = NotificationTopic.HOUSE_STEERING;
		NotificationTopic nationalPolicy = NotificationTopic.NATIONAL_POLICY;

		NotificationInfo info = adapter.retrieveNotificationInfosByTopics(Set.of(houseSteering, nationalPolicy));

		assertAll(
			() -> assertThat(info).isNotNull(),
			() -> assertThat(info.getInfos()).isNotEmpty().hasSize(1)
		);
	}
}