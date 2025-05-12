package com.barlow.services.auth.support.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.barlow.core.domain.Passport;
import com.barlow.services.auth.support.error.CoreAuthErrorType;
import com.barlow.services.auth.support.error.CoreAuthException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class GuestPassportAuthorizationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Passport passport = (Passport)request.getAttribute("passport");
		if (passport == null) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "passport is empty");
		}
		if (!passport.isAtLeastGuest()) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "user passport must be guest at least");
		}
		if (!passport.isValidOs()) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "user passport is invalid os");
		}
		return true;
	}
}
