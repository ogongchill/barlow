package com.barlow.services.support.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.barlow.services.support.Passport;
import com.barlow.services.support.annotation.PassportUser;
import com.barlow.services.support.error.CoreAuthErrorType;
import com.barlow.services.support.error.CoreAuthException;
import com.barlow.core.domain.User;

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
