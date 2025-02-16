package com.barlow.storage.db.core;

import java.util.Arrays;
import java.util.List;

import com.barlow.core.domain.notification.NotifiableTopic;

public enum NotificationTopic {
	/**
	 * 법안 알림 : [소관위접수] 의안에 한하여 알림
	 */
	HOUSE_STEERING("국회운영위원회", "default/icon-image-url"),
	LEGISLATION_AND_JUDICIARY("법제사법위원회", "default/icon-image-url"),
	NATIONAL_POLICY("정무위원회", "default/icon-image-url"),
	STRATEGY_AND_FINANCE("기획재정위원회", "default/icon-image-url"),
	EDUCATION("교육위원회", "default/icon-image-url"),
	SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS("과학기술정보방송통신위원회", "default/icon-image-url"),
	FOREIGN_AFFAIRS_AND_UNIFICATION("외교통일위원회", "default/icon-image-url"),
	NATIONAL_DEFENSE("국방위원회", "default/icon-image-url"),
	PUBLIC_ADMINISTRATION_AND_SECURITY("행정안전위원회", "default/icon-image-url"),
	CULTURE_SPORTS_AND_TOURISM("문화체육관광위원회", "default/icon-image-url"),
	AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES("농림축산식품해양수산위원", "default/icon-image-url"),
	TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS("산업통상자원중소벤처기업위원회", "default/icon-image-url"),
	HEALTH_AND_WELFARE("보건복지위원회", "default/icon-image-url"),
	ENVIRONMENT_AND_LABOR("환경노동위원회", "default/icon-image-url"),
	LAND_INFRASTRUCTURE_AND_TRANSPORT("국토교통위원회", "default/icon-image-url"),
	INTELLIGENCE("정보위원회", "default/icon-image-url"),
	GENDER_EQUALITY_FAMILY("여성가족위원회", "default/icon-image-url"),
	SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS("예산결산특별위원회", "default/icon-image-url"),

	/**
	 * 법안 알림 : default 로 5개 전부 동의된 알림 (수정가능)
	 */
	RECEIPT("접수", "default/icon-image-url"),
	SUBMISSION_PLENARY_SESSION("본회의부의안건", "default/icon-image-url"),
	RESOLUTION_PLENARY_SESSION("본회의의결", "default/icon-image-url"),
	RECONSIDERATION_GOVERNMENT("재의요구", "default/icon-image-url"),
	PROMULGATION("공포", "default/icon-image-url"),

	/**
	 * 사용자 상호작용 알림
	 */
	REACTION("리액션", "default/icon-image-url"),
	COMMENT("댓글", "default/icon-image-url"),
	;

	private static final int MAX_LEGISLATION_BODY_ORD = 17;

	private final String value;
	private final String iconUrl;

	public static NotificationTopic findByValue(String value) {
		return Arrays.stream(NotificationTopic.values())
			.filter(notificationTopic -> notificationTopic.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("기존에 존재하지 않던 NotificationTopic 입니다 : %s", value)
			));
	}

	static List<NotificationTopic> findDisableLegislationTopics(List<NotificationTopic> enableTopics) {
		return Arrays.stream(NotificationTopic.values())
			.filter(topic -> topic.ordinal() <= MAX_LEGISLATION_BODY_ORD && !enableTopics.contains(topic))
			.toList();
	}

	NotificationTopic(String value, String iconUrl) {
		this.value = value;
		this.iconUrl = iconUrl;
	}

	public String getValue() {
		return value;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public NotifiableTopic toNotifiableTopic() {
		return new NotifiableTopic(this.name(), this.value, this.iconUrl);
	}
}
