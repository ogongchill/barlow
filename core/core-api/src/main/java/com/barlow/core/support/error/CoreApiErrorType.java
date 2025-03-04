package com.barlow.core.support.error;

import static com.barlow.core.support.error.CoreApiErrorCode.E400;
import static com.barlow.core.support.error.CoreApiErrorCode.E401;
import static com.barlow.core.support.error.CoreApiErrorCode.E403;
import static com.barlow.core.support.error.CoreApiErrorCode.E404;
import static com.barlow.core.support.error.CoreApiErrorCode.E409;
import static com.barlow.core.support.error.CoreApiErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum CoreApiErrorType {

	DEFAULT_ERROR(INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, E400, "", WARN),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, E401, "", WARN),
	FORBIDDEN(HttpStatus.FORBIDDEN, E403, "", WARN),
	NOT_FOUND(HttpStatus.NOT_FOUND, E404, "", WARN),
	CONFLICT(HttpStatus.CONFLICT, E409, "", WARN),
	;

	private final HttpStatus status;
	private final CoreApiErrorCode code;
	private final String message;
	private final LogLevel logLevel;

	CoreApiErrorType(HttpStatus status, CoreApiErrorCode code, String message, LogLevel logLevel) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.logLevel = logLevel;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public CoreApiErrorCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}
}
