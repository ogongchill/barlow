package com.barlow.core.auth.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.barlow.core.auth.Passport;
import com.barlow.core.domain.User;
import com.barlow.core.support.annotation.PassportUser;
import com.barlow.core.support.error.CoreAuthErrorType;
import com.barlow.core.support.error.CoreAuthException;

@Component
public class PassportUserArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PassportUser.class)
			&& parameter.getParameterType().equals(User.class);
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
		return passport.getUser();
	}

	private void checkPassportExist(Passport passport, MethodParameter parameter) {
		if (passport == null) {
			throw new CoreAuthException(CoreAuthErrorType.DEFAULT_ERROR, "passport is empty", parameter);
		}
	}
}
