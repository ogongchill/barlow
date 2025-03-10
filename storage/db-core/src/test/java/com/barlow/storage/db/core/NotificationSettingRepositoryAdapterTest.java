package com.barlow.storage.db.core;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.LegislationNotificationSettingQuery;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.enumerate.LegislationType;
import com.barlow.core.enumerate.NotificationTopic;
import com.barlow.storage.db.CoreDbContextTest;
import com.barlow.storage.db.support.StorageTest;

import jakarta.transaction.Transactional;

@StorageTest("dummy/notificationSetting.json")
class NotificationSettingRepositoryAdapterTest extends CoreDbContextTest {

	private final NotificationSettingRepositoryAdapter adapter;
	private final NotificationConfigJpaRepository notificationConfigJpaRepository;

	public NotificationSettingRepositoryAdapterTest(
		NotificationSettingRepositoryAdapter adapter,
		NotificationConfigJpaRepository notificationConfigJpaRepository
	) {
		this.adapter = adapter;
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@DisplayName("입법계정 정보를 통해 회원이 해당 입법계정에 대한 알림을 설정했는지 조회한다")
	@ParameterizedTest
	@CsvSource(value = {"HOUSE_STEERING:true", "INTELLIGENCE:false"}, delimiter = ':')
	void retrieveNotificationSetting(String legislationTypeName, boolean expect) {
		User user = User.of(1L, User.Role.GUEST);
		LegislationNotificationSettingQuery query = new LegislationNotificationSettingQuery(
			LegislationType.valueOf(legislationTypeName), user
		);

		NotificationSetting notificationSetting = adapter.retrieveNotificationSetting(query);

		assertAll(
			() -> assertThat(notificationSetting).isNotNull(),
			() -> assertThat(notificationSetting.isNotifiable()).isEqualTo(expect)
		);
	}

	@DisplayName("회원이 입법계정에 대해 설정한 모든 알림설정을 조회한다")
	@Test
	void retrieveNotificationSettings() {
		User user = User.of(1L, User.Role.GUEST);

		assertThat(adapter.retrieveNotificationSettings(user))
			.isNotEmpty()
			.hasSize(18);
	}

	@DisplayName("회원이 알림주제(topic)을 통해 새롭게 알림을 설정한다")
	@Test
	@Transactional
	void saveNotificationSetting() {
		User user = User.of(1L, User.Role.GUEST);

		adapter.saveNotificationSetting(
			new NotificationSetting(user, NotificationTopic.STRATEGY_AND_FINANCE, true)
		);

		assertThat(notificationConfigJpaRepository.findAllByMemberNo(user.getUserNo()))
			.isNotEmpty()
			.hasSize(4);
	}

	@DisplayName("회원이 알림주제(topic)을 통해 기존의 알림을 삭제한다")
	@Test
	@Transactional
	void deleteNotificationSetting() {
		User user = User.of(1L, User.Role.GUEST);

		adapter.deleteNotificationSetting(
			new NotificationSetting(user, NotificationTopic.HOUSE_STEERING, false)
		);

		assertThat(notificationConfigJpaRepository.findAllByMemberNo(user.getUserNo()))
			.isNotEmpty()
			.hasSize(2);
	}
}