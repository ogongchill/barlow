package com.barlow.core.api.config;

import java.lang.reflect.Method;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import com.barlow.core.support.error.CoreApiException;
import com.barlow.support.alert.Alerter;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(AsyncExceptionHandler.class);
	private static final String CORE_API_ASYNC_EXCEPTION_MESSAGE_TEMPLATE = "CoreApiAsyncException : {}";

	private final Alerter alerter;

	public AsyncExceptionHandler(Alerter alerter) {
		this.alerter = alerter;
	}

	@Override
	public void handleUncaughtException(@NotNull Throwable ex, @NotNull Method method, Object @NotNull ... params) {
		if (ex instanceof CoreApiException coreApiException) {
			switch (coreApiException.getErrorType().getLogLevel()) {
				case ERROR -> {
					log.error(CORE_API_ASYNC_EXCEPTION_MESSAGE_TEMPLATE, ex.getMessage(), ex);
					alerter.alert(String.format("CoreApiAsyncUncaughtException : %s", ex.getMessage()));
				}
				case WARN -> log.warn(CORE_API_ASYNC_EXCEPTION_MESSAGE_TEMPLATE, ex.getMessage(), ex);
				default -> log.info(CORE_API_ASYNC_EXCEPTION_MESSAGE_TEMPLATE, ex.getMessage(), ex);
			}
		} else {
			log.error("Exception : {}", ex.getMessage(), ex);
			alerter.alert(String.format("Unknown AsyncUncaughtException : %s", ex.getMessage()));
		}
	}
}
