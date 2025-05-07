package com.barlow.core.storage;

import com.barlow.core.domain.User;
import com.barlow.core.domain.notificationsetting.NotificationSetting;
import com.barlow.core.enumerate.NotificationTopic;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_config")
public class NotificationConfigJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_config_no")
	private Long no;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "topic", nullable = false)
	private NotificationTopic topic;

	@Column(name = "enable", nullable = false)
	private Boolean enable;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	protected NotificationConfigJpaEntity() {
	}

	public NotificationConfigJpaEntity(NotificationTopic topic, Boolean enable, Long memberNo) {
		this.topic = topic;
		this.enable = enable;
		this.memberNo = memberNo;
	}

	public NotificationTopic getTopic() {
		return topic;
	}

	public Long getMemberNo() {
		return memberNo;
	}

	NotificationSetting toNotificationSetting(User user) {
		return new NotificationSetting(user, topic, enable);
	}
}
