package com.barlow.storage.db.core;

public enum NotificationTopic {
	/**
	 * 법안 알림 : default 로 5개 전부 동의된 알림 (수정가능)
	 */
	RECEIPT("접수의안"),
	SUBMISSION_PLENARY_SESSION("본회의부의안건"),
	RESOLUTION_PLENARY_SESSION("본회의의결"),
	RECONSIDERATION_GOVERNMENT("재의요구"),
	PROMULGATION("공포"),

	/**
	 * 법안 알림 : [소관위접수] 의안에 한하여 알림
	 */
	HOUSE_STEERING("국회운영위원회"),
	LEGISLATION_AND_JUDICIARY("법제사법위원회"),
	NATIONAL_POLICY("정무위원회"),
	STRATEGY_AND_FINANCE("기획재정위원회"),
	EDUCATION("교육위원회"),
	SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS("과학기술정보방송통신위원회"),
	FOREIGN_AFFAIRS_AND_UNIFICATION("외교통일위원회"),
	NATIONAL_DEFENSE("국방위원회"),
	PUBLIC_ADMINISTRATION_AND_SECURITY("행정안전위원회"),
	CULTURE_SPORTS_AND_TOURISM("문화체육관광위원회"),
	AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES("농림축산식품해양수산위원"),
	TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS("산업통상자원중소벤처기업위원회"),
	HEALTH_AND_WELFARE("보건복지위원회"),
	ENVIRONMENT_AND_LABOR("환경노동위원회"),
	LAND_INFRASTRUCTURE_AND_TRANSPORT("국토교통위원회"),
	INTELLIGENCE("정보위원회"),
	GENDER_EQUALITY_FAMILY("여성가족위원회"),
	SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS("예산결산특별위원회"),

	/**
	 * 사용자 상호작용 알림
	 */
	REACTION("리액션"),
	COMMENT("댓글"),
	;

	private final String value;

	NotificationTopic(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
