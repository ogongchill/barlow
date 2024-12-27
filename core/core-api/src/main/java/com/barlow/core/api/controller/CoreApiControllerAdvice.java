package com.barlow.core.api.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.barlow.core.support.error.CoreApiException;
import com.barlow.core.support.error.CoreApiErrorType;
import com.barlow.core.support.response.ApiResponse;

@RestControllerAdvice
public class CoreApiControllerAdvice {

	private static final Logger log = LoggerFactory.getLogger(CoreApiControllerAdvice.class);
	private static final String CORE_API_EXCEPTION_MESSAGE_TEMPLATE = "CoreApiException : {}";

	@ExceptionHandler(CoreApiException.class)
	public ResponseEntity<ApiResponse<?>> handleCoreException(CoreApiException e) {
		switch (e.getErrorType().getLogLevel()) {
			case ERROR -> log.error(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
			case WARN -> log.warn(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
			default -> log.info(CORE_API_EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), e);
		}
		return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
		log.error("Exception : {}", e.getMessage(), e);
		return new ResponseEntity<>(ApiResponse.error(
			CoreApiErrorType.DEFAULT_ERROR),
			CoreApiErrorType.DEFAULT_ERROR.getStatus()
		);
	}
}
