package com.barlow.storage.db.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_center")
public class NotificationCenterJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_center_no")
	private Long no;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	@Enumerated(EnumType.STRING)
	@Column(name = "notification_topic", nullable = false)
	private NotificationTopic notificationTopic;

	@Column(name = "icon_url", nullable = false)
	private String iconUrl;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "body", nullable = false)
	private String body;

	protected NotificationCenterJpaEntity() {
	}
}
