package com.barlow.core.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.barlow.core.auth.Passport;
import com.barlow.core.support.error.CoreAuthErrorType;
import com.barlow.core.support.error.CoreAuthException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class MemberPassportAuthorizationInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Passport passport = (Passport)request.getAttribute("passport");
		if (passport == null) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "passport is empty");
		}
		if (passport.isForGuest()) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "this service is for member");
		}
		if (!passport.isValidOs()) {
			throw new CoreAuthException(CoreAuthErrorType.UNAUTHORIZED, "user passport is invalid os");
		}
		return true;
	}
}
