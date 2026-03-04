package com.barlow.infra.auth.authentication.core;

import com.barlow.infra.auth.support.error.CoreAuthErrorType;
import com.barlow.infra.auth.support.error.CoreAuthException;

public class AuthenticationException extends CoreAuthException {

	private final AuthenticationExceptionType type;

	public AuthenticationException(CoreAuthErrorType errorType, String errorMessage, AuthenticationExceptionType type) {
		super(errorType, errorMessage);
		this.type = type;
	}

	public AuthenticationException(CoreAuthErrorType errorType, String errorMessage, Object data,
		AuthenticationExceptionType type) {
		super(errorType, errorMessage, data);
		this.type = type;
	}

	public AuthenticationException(String message, AuthenticationExceptionType type) {
		super(CoreAuthErrorType.UNAUTHORIZED, message);
		this.type = type;
	}

	public AuthenticationExceptionType getType() {
		return type;
	}
}
