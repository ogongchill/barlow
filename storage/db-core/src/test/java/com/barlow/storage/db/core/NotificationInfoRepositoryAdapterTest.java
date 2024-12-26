package com.barlow.storage.db.core;

import static com.barlow.storage.db.core.DeviceJpaEntity.Status.ACTIVE;
import static com.barlow.storage.db.core.DeviceJpaEntity.Status.INACTIVE;
import static com.barlow.storage.db.core.DeviceOs.ANDROID;
import static com.barlow.storage.db.core.DeviceOs.IOS;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import com.barlow.notification.NotificationInfo;
import com.barlow.storage.db.CoreDbContextTest;

@Transactional
class NotificationInfoRepositoryAdapterTest extends CoreDbContextTest {

	private final NotificationInfoRepositoryAdapter notificationInfoRepositoryAdapter;
	private final NotificationConfigJpaRepository notificationConfigJpaRepository;
	private final DeviceJpaRepository deviceJpaRepository;

	public NotificationInfoRepositoryAdapterTest(
		NotificationInfoRepositoryAdapter notificationInfoRepositoryAdapter,
		NotificationConfigJpaRepository notificationConfigJpaRepository,
		DeviceJpaRepository deviceJpaRepository
	) {
		this.notificationInfoRepositoryAdapter = notificationInfoRepositoryAdapter;
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
		this.deviceJpaRepository = deviceJpaRepository;
	}

	@DisplayName("회원이 두 개 이상의 디바이스를 가지고 있어도, ACTIVE 상태 디바이스에 대해서만 알림 정보를 조회한다")
	@Test
	void retrieveNotificationInfosByTopic1() {
		NotificationTopic topic = NotificationTopic.RECEIPT;
		notificationConfigJpaRepository.saveAndFlush(new NotificationConfigJpaEntity(topic, true, 1L));
		deviceJpaRepository.saveAllAndFlush(List.of(
			new DeviceJpaEntity("deviceId1", IOS, "token1", 1L, ACTIVE),
			new DeviceJpaEntity("deviceId2", ANDROID, "token2", 1L, INACTIVE)
		));

		List<NotificationInfo> infos = notificationInfoRepositoryAdapter.retrieveNotificationInfosByTopic(topic.name());

		NotificationInfo expect = new NotificationInfo(
			new NotificationInfo.Topic(topic.getValue(), null, null),
			new NotificationInfo.Subscriber(1L, IOS.name(), "token1")
		);
		assertAll(
			() -> assertThat(infos).hasSize(1),
			() -> assertThat(infos.getFirst()).isEqualTo(expect)
		);
	}

	@DisplayName("푸시알림 동의가 false 인 회원은 알림 정보를 조회하지 않는다")
	@Test
	void retrieveNotificationInfosByTopic2() {
		NotificationTopic topic = NotificationTopic.RECEIPT;
		notificationConfigJpaRepository.saveAndFlush(new NotificationConfigJpaEntity(topic, false, 1L));
		deviceJpaRepository.saveAndFlush(new DeviceJpaEntity("deviceId1", IOS, "token1", 1L, ACTIVE));

		List<NotificationInfo> infos = notificationInfoRepositoryAdapter.retrieveNotificationInfosByTopic(topic.name());

		assertThat(infos).isEmpty();
	}

	@DisplayName("NotificationTopic 들로 알림 정보를 조회하면 해당 토픽을 구독한 회원들의 알림설정 정보를 조회한다")
	@Test
	void retrieveNotificationInfosByTopics() {
		NotificationTopic houseSteering = NotificationTopic.HOUSE_STEERING;
		NotificationTopic nationalPolicy = NotificationTopic.NATIONAL_POLICY;
		notificationConfigJpaRepository.saveAllAndFlush(List.of(
			new NotificationConfigJpaEntity(houseSteering, true, 1L),
			new NotificationConfigJpaEntity(nationalPolicy, true, 1L),
			new NotificationConfigJpaEntity(houseSteering, true, 2L)
		));
		deviceJpaRepository.saveAllAndFlush(List.of(
			new DeviceJpaEntity("deviceId1", IOS, "token1", 1L, ACTIVE),
			new DeviceJpaEntity("deviceId2", ANDROID, "token2", 2L, ACTIVE)
		));

		List<NotificationInfo> infos = notificationInfoRepositoryAdapter.retrieveNotificationInfosByTopics(
			Set.of(houseSteering.name(), nationalPolicy.name())
		);

		assertThat(infos).hasSize(3);
	}
}