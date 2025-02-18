package com.barlow.storage.db.core;

import com.barlow.core.domain.home.notificationcenter.NotificationCenterItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "notification_center_item")
public class NotificationCenterItemJpaEntity extends BaseTimeJpaEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "notification_center_item_no")
	private Long no;

	@Column(name = "member_no", nullable = false)
	private Long memberNo;

	@Column(name = "bill_id", nullable = false)
	private String billId;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition = "varchar(100)", name = "notification_topic", nullable = false)
	private NotificationTopic notificationTopic;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "body", nullable = false)
	private String body;

	protected NotificationCenterItemJpaEntity() {
	}

	NotificationCenterItem toNotificationItem() {
		return new NotificationCenterItem(
			billId,
			notificationTopic.name(),
			notificationTopic.getIconPath(),
			title,
			body,
			getCreatedAt()
		);
	}
}
