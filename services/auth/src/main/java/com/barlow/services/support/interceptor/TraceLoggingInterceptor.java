package com.barlow.services.support.interceptor;

import java.util.Map;

import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.barlow.services.support.Passport;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class TraceLoggingInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler
	) {
		Passport passport = (Passport)request.getAttribute("passport");
		ThreadContext.putAll(Map.of(
			"api", request.getRequestURI(),
			"user", String.valueOf(passport.getUser().getUserNo())
		));
		return true;
	}

	@Override
	public void afterCompletion(
		HttpServletRequest request,
		HttpServletResponse response,
		Object handler,
		Exception ex
	) {
		ThreadContext.clearAll();
	}
}
