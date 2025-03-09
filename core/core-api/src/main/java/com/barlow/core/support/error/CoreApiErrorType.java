package com.barlow.core.support.error;

import static com.barlow.core.support.error.CoreApiErrorCode.E400;
import static com.barlow.core.support.error.CoreApiErrorCode.E401;
import static com.barlow.core.support.error.CoreApiErrorCode.E403;
import static com.barlow.core.support.error.CoreApiErrorCode.E404;
import static com.barlow.core.support.error.CoreApiErrorCode.E409;
import static com.barlow.core.support.error.CoreApiErrorCode.E500;
import static org.springframework.boot.logging.LogLevel.ERROR;
import static org.springframework.boot.logging.LogLevel.WARN;

import java.util.Arrays;

import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

import com.barlow.core.exception.CoreDomainExceptionCode;

public enum CoreApiErrorType {

	DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, E500, "An unexpected error has occurred.", ERROR),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, E400, "Invalid request parameters or payload.", WARN),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, E401, "Authentication is required and has failed or not been provided.", WARN),
	FORBIDDEN(HttpStatus.FORBIDDEN, E403, "You do not have permission to access this resource.", WARN),
	NOT_FOUND(HttpStatus.NOT_FOUND, E404, "The requested resource could not be found.", WARN),
	CONFLICT(HttpStatus.CONFLICT, E409, "The request conflicts with the current state of the resource.", WARN);
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

	public static CoreApiErrorType findByErrorCode(CoreDomainExceptionCode code) {
		return Arrays.stream(CoreApiErrorType.values())
			.filter(type -> type.code.name().equals(code.name()))
			.findFirst()
			.orElse(CoreApiErrorType.DEFAULT_ERROR);
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
