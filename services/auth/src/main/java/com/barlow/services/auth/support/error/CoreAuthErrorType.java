package com.barlow.services.auth.support.error;

import static com.barlow.services.auth.support.error.CoreAuthErrorCode.E400;
import static com.barlow.services.auth.support.error.CoreAuthErrorCode.E401;
import static com.barlow.services.auth.support.error.CoreAuthErrorCode.E404;
import static com.barlow.services.auth.support.error.CoreAuthErrorCode.E409;
import static com.barlow.services.auth.support.error.CoreAuthErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum CoreAuthErrorType {

	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, E500, ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, E400, WARN),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, E401, WARN),
	NOT_FOUND(HttpStatus.NOT_FOUND, E404, WARN),
	CONFLICT(HttpStatus.CONFLICT, E409, WARN),
	;

	private final HttpStatus status;
	private final CoreAuthErrorCode code;
	private final LogLevel logLevel;

	CoreAuthErrorType(HttpStatus status, CoreAuthErrorCode code, LogLevel logLevel) {
		this.status = status;
		this.code = code;
		this.logLevel = logLevel;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public CoreAuthErrorCode getCode() {
		return code;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}
}
