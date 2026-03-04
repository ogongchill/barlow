package com.barlow.infra.auth.support.error;

import static com.barlow.infra.auth.support.error.CoreAuthErrorCode.E400;
import static com.barlow.infra.auth.support.error.CoreAuthErrorCode.E401;
import static com.barlow.infra.auth.support.error.CoreAuthErrorCode.E404;
import static com.barlow.infra.auth.support.error.CoreAuthErrorCode.E409;
import static com.barlow.infra.auth.support.error.CoreAuthErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

public enum CoreAuthErrorType {

	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, E400, "Invalid request parameters or payload.", WARN),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, E401, "Authentication is required and has failed or not been provided.", WARN),
	NOT_FOUND(HttpStatus.NOT_FOUND, E404, "The requested resource could not be found.", WARN),
	CONFLICT(HttpStatus.CONFLICT, E409, "The request conflicts with the current state of the resource.", WARN),
	;

	private final HttpStatus status;
	private final CoreAuthErrorCode code;
	private final String message;
	private final LogLevel logLevel;

	CoreAuthErrorType(HttpStatus status, CoreAuthErrorCode code, String message, LogLevel logLevel) {
		this.status = status;
		this.code = code;
		this.message = message;
		this.logLevel = logLevel;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public CoreAuthErrorCode getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public LogLevel getLogLevel() {
		return logLevel;
	}
}
