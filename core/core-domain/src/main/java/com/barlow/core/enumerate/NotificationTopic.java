package com.barlow.core.enumerate;

import java.util.Arrays;
import java.util.List;

public enum NotificationTopic {
	/**
	 * 법안 알림 : [소관위접수] 의안에 한하여 알림
	 */
	HOUSE_STEERING(LegislationType.HOUSE_STEERING.getValue(), "default/icon-image-url"),
	LEGISLATION_AND_JUDICIARY(LegislationType.LEGISLATION_AND_JUDICIARY.getValue(), "default/icon-image-url"),
	NATIONAL_POLICY(LegislationType.NATIONAL_POLICY.getValue(), "default/icon-image-url"),
	STRATEGY_AND_FINANCE(LegislationType.STRATEGY_AND_FINANCE.getValue(), "default/icon-image-url"),
	EDUCATION(LegislationType.EDUCATION.getValue(), "default/icon-image-url"),
	SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS(LegislationType.SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS.getValue(), "default/icon-image-url"),
	FOREIGN_AFFAIRS_AND_UNIFICATION(LegislationType.FOREIGN_AFFAIRS_AND_UNIFICATION.getValue(), "default/icon-image-url"),
	NATIONAL_DEFENSE(LegislationType.NATIONAL_DEFENSE.getValue(), "default/icon-image-url"),
	PUBLIC_ADMINISTRATION_AND_SECURITY(LegislationType.PUBLIC_ADMINISTRATION_AND_SECURITY.getValue(), "default/icon-image-url"),
	CULTURE_SPORTS_AND_TOURISM(LegislationType.CULTURE_SPORTS_AND_TOURISM.getValue(), "default/icon-image-url"),
	AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES(LegislationType.AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES.getValue(), "default/icon-image-url"),
	TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS(LegislationType.TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS.getValue(), "default/icon-image-url"),
	HEALTH_AND_WELFARE(LegislationType.HEALTH_AND_WELFARE.getValue(), "default/icon-image-url"),
	ENVIRONMENT_AND_LABOR(LegislationType.ENVIRONMENT_AND_LABOR.getValue(), "default/icon-image-url"),
	LAND_INFRASTRUCTURE_AND_TRANSPORT(LegislationType.LAND_INFRASTRUCTURE_AND_TRANSPORT.getValue(), "default/icon-image-url"),
	INTELLIGENCE(LegislationType.INTELLIGENCE.getValue(), "default/icon-image-url"),
	GENDER_EQUALITY_FAMILY(LegislationType.GENDER_EQUALITY_FAMILY.getValue(), "default/icon-image-url"),
	SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS(LegislationType.SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS.getValue(), "default/icon-image-url"),

	/**
	 * 법안 알림 : default 로 5개 전부 동의된 알림 (수정가능)
	 */
	RECEIPT(ProgressStatus.RECEIVED.getValue(), "default/icon-image-url"),
	SUBMISSION_PLENARY_SESSION(ProgressStatus.PLENARY_SUBMITTED.getValue(), "default/icon-image-url"),
	RESOLUTION_PLENARY_SESSION(ProgressStatus.PLENARY_DECIDED.getValue(), "default/icon-image-url"),
	RECONSIDERATION_GOVERNMENT(ProgressStatus.REDEMAND_REQUESTED.getValue(), "default/icon-image-url"),
	PROMULGATION(ProgressStatus.PROMULGATED.getValue(), "default/icon-image-url"),

	/**
	 * 사용자 상호작용 알림
	 */
	REACTION("리액션", "default/icon-image-url"),
	COMMENT("댓글", "default/icon-image-url"),
	;

	private static final int MAX_LEGISLATION_BODY_ORD = 17;

	private final String value;
	private final String iconPath;

	public static NotificationTopic findByValue(String value) {
		return Arrays.stream(NotificationTopic.values())
			.filter(notificationTopic -> notificationTopic.value.equals(value))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("기존에 존재하지 않던 NotificationTopic 입니다 : %s", value)
			));
	}

	public static List<NotificationTopic> findDisableLegislationTopics(List<NotificationTopic> enableTopics) {
		return Arrays.stream(NotificationTopic.values())
			.filter(topic -> topic.ordinal() <= MAX_LEGISLATION_BODY_ORD && !enableTopics.contains(topic))
			.toList();
	}

	public static NotificationTopic findByLegislationType(LegislationType legislationType) {
		return Arrays.stream(NotificationTopic.values())
			.filter(topic -> topic.value.equals(legislationType.getValue()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("%s 에 대응하는 NotificationTopic 이 존재하지 않습니다", legislationType)
			));
	}

	public static NotificationTopic findByProgressStatus(ProgressStatus progressStatus) {
		return Arrays.stream(NotificationTopic.values())
			.filter(topic -> topic.value.equals(progressStatus.getValue()))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException(
				String.format("%s 에 대응하는 NotificationTopic 이 존재하지 않습니다", progressStatus)
			));
	}

	public static boolean isNotifiableProgressStatus(ProgressStatus progressStatus) {
		return Arrays.stream(NotificationTopic.values())
			.anyMatch(topic -> topic.value.equals(progressStatus.getValue()));
	}

	public boolean isRelatedCommittee() {
		return this.ordinal() <= MAX_LEGISLATION_BODY_ORD;
	}

	NotificationTopic(String value, String iconPath) {
		this.value = value;
		this.iconPath = iconPath;
	}

	public String getValue() {
		return value;
	}

	public String getIconPath() {
		return iconPath;
	}
}
