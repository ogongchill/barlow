package com.barlow.services.auth.support.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.barlow.core.domain.Passport;
import com.barlow.services.auth.support.annotation.PassportUser;
import com.barlow.services.auth.support.error.CoreAuthErrorType;
import com.barlow.services.auth.support.error.CoreAuthException;

@Component
public class PassportUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PassportUser.class)
			&& parameter.getParameterType().equals(Passport.class);
	}

	@Override
	public Object resolveArgument(
		MethodParameter parameter,
		ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest,
		WebDataBinderFactory binderFactory
	) {
		Passport passport = (Passport)webRequest.getAttribute("passport", RequestAttributes.SCOPE_REQUEST);
		checkPassportExist(passport, parameter);
		return passport;
	}

	private void checkPassportExist(Passport passport, MethodParameter parameter) {
		if (passport == null) {
			throw new CoreAuthException(CoreAuthErrorType.DEFAULT_ERROR, "passport is empty", parameter);
		}
	}
}
