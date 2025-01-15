package com.barlow.core.api.controller;

import static com.barlow.core.support.error.CoreApiErrorType.BAD_REQUEST;
import static com.barlow.core.support.error.CoreApiErrorType.CONFLICT;
import static com.barlow.core.support.error.CoreApiErrorType.NOT_FOUND;
import static com.barlow.core.support.error.CoreApiErrorType.UNAUTHORIZED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.core.support.response.ApiResponse;

@RestControllerAdvice
public class CoreDomainControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(CoreDomainControllerAdvice.class);

	@ExceptionHandler(CoreDomainException.class)
	public ResponseEntity<ApiResponse<?>> handleCoreException(CoreDomainException e) {
		switch (e.getLevel()) {
			case BUSINESS -> log.warn("Business exception : {}", e.getMessage(), e);
			case IMPLEMENTATION -> log.warn("Implementation exception : {}", e.getMessage(), e);
			default -> log.warn("Unknown exception : {}", e.getMessage(), e);
		}
		return switch (e.getCode()) {
			case E400 -> new ResponseEntity<>(ApiResponse.error(BAD_REQUEST), BAD_REQUEST.getStatus());
			case E401 -> new ResponseEntity<>(ApiResponse.error(UNAUTHORIZED), UNAUTHORIZED.getStatus());
			case E404 -> new ResponseEntity<>(ApiResponse.error(NOT_FOUND), NOT_FOUND.getStatus());
			case E409 -> new ResponseEntity<>(ApiResponse.error(CONFLICT), CONFLICT.getStatus());
		};
	}
}
