package com.barlow.storage.db.core;

import java.util.List;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import com.barlow.core.domain.User;
import com.barlow.core.domain.legislationaccount.notificationsetting.NotificationSetting;
import com.barlow.core.domain.legislationaccount.notificationsetting.LegislationNotificationSettingQuery;
import com.barlow.core.domain.legislationaccount.notificationsetting.NotificationSettingRepository;

@Component
public class NotificationSettingRepositoryAdapter implements NotificationSettingRepository {

	private final NotificationConfigJpaRepository notificationConfigJpaRepository;

	public NotificationSettingRepositoryAdapter(NotificationConfigJpaRepository notificationConfigJpaRepository) {
		this.notificationConfigJpaRepository = notificationConfigJpaRepository;
	}

	@Override
	@NotNull
	public NotificationSetting retrieveNotificationSetting(LegislationNotificationSettingQuery query) {
		User user = query.user();
		NotificationTopic topic = NotificationTopic.valueOf(query.committeeName().toUpperCase());
		NotificationConfigJpaEntity notificationConfigJpaEntity = notificationConfigJpaRepository
			.findByTopicAndMemberNo(topic, user.getUserNo());
		if (notificationConfigJpaEntity == null) {
			return new NotificationSetting(user, topic.getValue(), topic.getIconUrl(), false);
		}
		return notificationConfigJpaEntity.toNotificationSetting(user);
	}

	@Override
	public List<NotificationSetting> retrieveNotificationSettings(User user) {
		List<NotificationConfigJpaEntity> jpaEntities = notificationConfigJpaRepository.findAllByMemberNo(user.getUserNo());
		List<NotificationTopic> enableTopics = jpaEntities.stream()
			.map(NotificationConfigJpaEntity::getTopic)
			.toList();
		List<NotificationTopic> disableTopics = NotificationTopic.findDisableLegislationTopics(enableTopics);
		return Stream.concat(
			jpaEntities.stream().map(jpaEntity -> jpaEntity.toNotificationSetting(user)),
			disableTopics.stream().map(disableTopic -> new NotificationSetting(
				user,
				disableTopic.getValue(),
				disableTopic.getIconUrl(),
				false
			))
		).toList();
	}

	@Override
	public void saveNotificationSetting(NotificationSetting notificationSetting) {
		NotificationTopic topic = NotificationTopic.findByValue(notificationSetting.getTopicName());
		notificationConfigJpaRepository.save(
			new NotificationConfigJpaEntity(topic, true, notificationSetting.getUser().getUserNo())
		);
	}

	@Override
	public void deleteNotificationSetting(NotificationSetting notificationSetting) {
		notificationConfigJpaRepository.deleteByTopicAndMemberNo(
			NotificationTopic.findByValue(notificationSetting.getTopicName()),
			notificationSetting.getUser().getUserNo()
		);
	}
}
