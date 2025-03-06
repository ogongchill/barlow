package com.barlow.client.knal.opendata.api.code;

public enum CommitteeCode {

	HOUSE_STEERING("국회운영위원회", "9700005"),
	LEGISLATION_AND_JUDICIARY("법제사법위원회", "9700006"),
	NATIONAL_POLICY("정무위원회", "9700008"),
	STRATEGY_AND_FINANCE("기획재정위원회", "9700300"),
	EDUCATION("교육위원회", "9700300"),
	SCIENCE_ICT_BROADCASTING_AND_COMMUNICATIONS("과학기술정보방송통신위원회", "9700479"),
	FOREIGN_AFFAIRS_AND_UNIFICATION("외교통일위원회", "9700409"),
	NATIONAL_DEFENSE("국방위원회", "9700019"),
	PUBLIC_ADMINISTRATION_AND_SECURITY("행정안전위원회", "9700480"),
	CULTURE_SPORTS_AND_TOURISM("문화체육관광위원회", "9700513"),
	AGRICULTURE_FOOD_RURAL_AFFAIRS_OCEANS_AND_FISHERIES("농림축산식품해양수산위원회", "9700408"),
	TRADE_INDUSTRY_ENERGY_SMES_AND_STARTUPS("산업통상자원중소벤처기업위원회", "9700481"),
	HEALTH_AND_WELFARE("보건복지위원회", "9700341"),
	ENVIRONMENT_AND_LABOR("환경노동위원회", "9700038"),
	LAND_INFRASTRUCTURE_AND_TRANSPORT("국토교통위원회", "9700407"),
	INTELLIGENCE("정보위원회", "9700047"),
	GENDER_EQUALITY_FAMILY("여성가족위원회", "9700342"),
	SPECIAL_COMMITTEE_ON_BUDGET_ACCOUNTS("예산결산특별위원회", "9700049");

	private final String description;
	private final String code;

	CommitteeCode(String description, String code) {
		this.description = description;
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public String getCode() {
		return code;
	}
}
