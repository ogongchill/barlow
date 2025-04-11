package com.barlow.client.knal.opendata.api.code;

import java.util.Arrays;

public enum ErrorCode {

	APPLICATION_ERROR("01", "어플리케이션 에러"),
	HTTP_ERROR("04", "HTTP 에러"),
	NO_OPENAPI_SERVICE_ERROR("12", "해당 오픈 API 서비스가 없거나 폐기됨"),
	SERVICE_ACCESS_DENIED_ERROR("20", "서비스 접근거부"),
	LIMIT_NUMBER_OF_SERVICE_REQUESTS_EXCEEDS_ERROR("22", "서비스 요청제한횟수 초과에러"),
	LIMIT_NUMBER_OF_SERVICE_REQUESTS_PER_SECOND_EXCEEDS_ERROR("23", "최대동시 요청수 초과에러"),
	SERVICE_KEY_IS_NOT_REGISTERED_ERROR("30", "등록되지 않은 서비스키"),
	DEADLINE_HAS_EXPIRED_ERROR("31", "활용기간 만료"),
	UNREGISTERED_IP_ERROR("32", "등록되지 않은 IP"),
	UNKNOWN_ERROR("99", "기타 에러"),
	;

	public static boolean isNameContainedInValue(String xmlResponseBody) {
		return Arrays.stream(values())
			.anyMatch(errorCode -> xmlResponseBody.contains(errorCode.name()));
	}

	public boolean isRetryable() {
		return this.code.equals("01")
			|| this.code.equals("04")
			|| this.code.equals("30")
			|| this.code.equals("99");
	}

	public String getCode() {
		return this.code;
	}

	private final String code;
	private final String description;

	ErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}
}
