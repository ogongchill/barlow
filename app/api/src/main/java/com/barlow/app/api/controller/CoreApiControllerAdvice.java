package com.barlow.app.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.barlow.core.exception.CoreDomainException;
import com.barlow.app.support.error.CoreApiException;
import com.barlow.app.support.error.CoreApiErrorType;
import com.barlow.app.support.response.ApiResponse;
import com.barlow.services.auth.support.error.CoreAuthException;
import com.barlow.support.alert.Alerter;

@RestControllerAdvice
public class CoreApiControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(CoreApiControllerAdvice.class);
	private static final String CORE_API_EXCEPTION_MESSAGE_TEMPLATE = "CoreApiException : {}";
	private static final String CORE_AUTH_EXCEPTION_MESSAGE_TEMPLATE = "CoreAuthException : {}";

	private final Alerter alerter;

	public CoreApiControllerAdvice(Alerter alerter) {
		this.alerter = alerter;
	}

	@ExceptionHandler(CoreApiException.class)
	public ResponseEntity<ApiResponse<Void>> handleCoreException(CoreApiException e) {
		switch (e.getErrorType().getLogLevel()) {
			case ERROR -> {
				log.error(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
				alerter.alert(String.format("CoreApiException : %s", e.getMessage()));
			}
			case WARN -> log.warn(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
			default -> log.info(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
		}
		return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
	}

	@ExceptionHandler(CoreAuthException.class)
	public ResponseEntity<ApiResponse<Object>> handleCoreException(CoreAuthException e) {
		switch (e.getErrorType().getLogLevel()) {
			case ERROR -> {
				log.error(CORE_AUTH_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
				alerter.alert(String.format("CoreAuthException : %s", e.getMessage()));
			}
			case WARN -> log.warn(CORE_AUTH_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
			default -> log.info(CORE_AUTH_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
		}
		return new ResponseEntity<>(
			ApiResponse.error(e.getErrorCode(), e.getErrorMessage(), e.getData()),
			e.getErrorStatus()
		);
	}

	@ExceptionHandler(CoreDomainException.class)
	public ResponseEntity<ApiResponse<Void>> handleCoreException(CoreDomainException e) {
		switch (e.getLevel()) {
			case BUSINESS -> log.warn("Business exception : {}", e.getMessage(), e);
			case IMPLEMENTATION -> log.warn("Implementation exception : {}", e.getMessage(), e);
			default -> log.warn("Unknown exception : {}", e.getMessage(), e);
		}
		return new ResponseEntity<>(
			ApiResponse.error(e.getCode().name(), e.getMessage()),
			CoreApiErrorType.findByErrorCode(e.getCode()).getStatus()
		);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
		log.error("Exception : {}", e.getMessage(), e);
		alerter.alert(String.format("Unexpected Exception : %s", e.getMessage()));
		return new ResponseEntity<>(ApiResponse.error(
			CoreApiErrorType.DEFAULT_ERROR),
			CoreApiErrorType.DEFAULT_ERROR.getStatus()
		);
	}
}
