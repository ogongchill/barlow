package com.barlow.storage.db.core;

public enum LegislationType {

	GOVERNMENT("정부", "default/icon-image-url"),
	SPEAKER("국회의장", "default/icon-image-url"),

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

	EMPTY("소관위미접수상태", "default/icon-image-url"),
	;

	private final String value;
	private final String iconUrl;

	LegislationType(String value, String iconUrl) {
		this.value = value;
		this.iconUrl = iconUrl;
	}

	String getValue() {
		return value;
	}

	String getIconUrl() {
		return iconUrl;
	}
}
