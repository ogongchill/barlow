package com.barlow.client.knal.opencongress.api.error;

import java.util.Arrays;

public enum ErrorCode {

	MISSING_REQUIRED_VALUE("ERROR-300", "필수 값이 누락되어 있습니다. 요청인자를 참고 하십시오."),
	INVALID_API_KEY("ERROR-290", "인증키가 유효하지 않습니다. 인증키가 없는 경우, 홈페이지에서 인증키를 신청하십시오."),
	TRAFFIC_LIMIT_EXCEEDED("ERROR-337", "일별 트래픽 제한을 넘은 호출입니다. 오늘은 더이상 호출할 수 없습니다."),
	SERVICE_NOT_FOUND("ERROR-310", "해당하는 서비스를 찾을 수 없습니다. 요청인자 중 SERVICE 를 확인하십시오."),
	INVALID_REQUEST_PARAMETER("ERROR-333", "요청위치 값의 타입이 유효하지 않습니다. 요청위치 값은 정수를 입력하세요."),
	EXCEEDS_MAX_ROWS("ERROR-336", "데이터요청은 한번에 최대 1,000건을 넘을 수 없습니다."),
	SERVER_ERROR("ERROR-500", "서버 오류입니다. 지속적으로 발생 시 홈페이지로 문의(Q&A) 바랍니다."),
	DATABASE_CONNECTION_ERROR("ERROR-600", "데이터베이스 연결 오류입니다. 지속적으로 발생 시 홈페이지로 문의(Q&A) 바랍니다."),
	SQL_SYNTAX_ERROR("ERROR-601", "SQL 문장 오류 입니다. 지속적으로 발생 시 홈페이지로 문의(Q&A) 바랍니다."),
	EXPIRED_API_KEY("ERROR-990", "인증서가 폐기되었습니다. 홈페이지에서 인증키를 확인하십시오.");

	private final String code;
	private final String description;

	public static boolean isNameContainedInValue(String xmlResponseBody) {
		return Arrays.stream(values())
			.anyMatch(errorCode -> xmlResponseBody.contains(errorCode.code));
	}

	ErrorCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String code() {
		return code;
	}

	public String description() {
		return description;
	}
}
