package com.barlow.core.enumerate;

import java.util.Arrays;
import java.util.List;

public enum LegislationType {

	HOUSE_STEERING("국회운영위원회", "국회운영위원장", "default/icon-image-url"),
	LEGISLATION_AND_JUDICIARY("법제사법위원회", "법제사법위원장", "default/icon-image-url"),
	NATIONAL_POLICY("정무위원회", "정무위원장", "default/icon-image-url"),
	STRATEGY_AND_FINANCE("기획재정위원회", "기획재정위원장", "default/icon-image-url"),
	EDUCATION("교육위원회", "교육위원장", "default/icon-image-url"),
	SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS("과학기술정보방송통신위원회", "과학기술정보방송통신위원장", "default/icon-image-url"),
	FOREIGN_AFFAIRS_AND_UNIFICATION("외교통일위원회", "외교통일위원장", "default/icon-image-url"),
	NATIONAL_DEFENSE("국방위원회", "국방위원장", "default/icon-image-url"),
	PUBLIC_ADMINISTRATION_AND_SECURITY("행정안전위원회", "행정안전위원장", "default/icon-image-url"),
	CULTURE_SPORTS_AND_TOURISM("문화체육관광위원회", "문화체육관광위원장", "default/icon-image-url"),
	AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES("농림축산식품해양수산위원회", "농림축산식품해양수산위원장", "default/icon-image-url"),
	TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS("산업통상자원중소벤처기업위원회", "산업통상자원중소벤처기업위원장", "default/icon-image-url"),
	HEALTH_AND_WELFARE("보건복지위원회", "보건복지위원장", "default/icon-image-url"),
	ENVIRONMENT_AND_LABOR("환경노동위원회", "환경노동위원장", "default/icon-image-url"),
	LAND_INFRASTRUCTURE_AND_TRANSPORT("국토교통위원회", "국토교통위원장", "default/icon-image-url"),
	INTELLIGENCE("정보위원회", "정보위원장", "default/icon-image-url"),
	GENDER_EQUALITY_FAMILY("여성가족위원회", "여성가족위원장", "default/icon-image-url"),
	SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS("예산결산특별위원회", "예산결산특별위원장", "default/icon-image-url"),

	GOVERNMENT("정부", null, "default/icon-image-url"),
	SPEAKER("국회의장", null, "default/icon-image-url"),

	EMPTY("소관위미접수상태", null, "default/icon-image-url"),
	SPECIAL_COMMITTEE("특별위원회", "특별위원장", "default/icon-image-url"),
	;

	private static final int MAX_LEGISLATION_BODY_ORD = 17;

	private final String value;
	private final String chairman;
	private final String iconPath;

	public static LegislationType findByValue(String value) {
		return Arrays.stream(LegislationType.values())
			.filter(body -> body.value.equals(value))
			.findFirst()
			.orElse(EMPTY);
	}

	public static LegislationType findByChairman(String chairman) {
		return Arrays.stream(LegislationType.values())
			.filter(body -> body.ordinal() <= MAX_LEGISLATION_BODY_ORD && body.chairman.equals(chairman))
			.findFirst()
			.orElse(SPECIAL_COMMITTEE);
	}

	public long getLegislationNo() {
		return Integer.toUnsignedLong(this.ordinal() + 1);
	}

	public static List<LegislationType> findDisableLegislationType(List<LegislationType> activeLegislationBodies) {
		return Arrays.stream(LegislationType.values())
			.filter(body -> body.ordinal() <= MAX_LEGISLATION_BODY_ORD && !activeLegislationBodies.contains(body))
			.toList();
	}

	public boolean isEmpty() {
		return this == EMPTY;
	}

	LegislationType(String value, String chairman, String iconPath) {
		this.value = value;
		this.chairman = chairman;
		this.iconPath = iconPath;
	}

	public String getValue() {
		return value;
	}

	public String getIconPath() {
		return iconPath;
	}
}
