package com.barlow.app.api.config;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.barlow.services.auth.support.interceptor.GuestPassportAuthorizationInterceptor;
import com.barlow.services.auth.support.interceptor.MemberPassportAuthorizationInterceptor;
import com.barlow.services.auth.support.interceptor.TraceLoggingInterceptor;
import com.barlow.services.auth.support.resolver.PassportUserArgumentResolver;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

	private final GuestPassportAuthorizationInterceptor guestPassportAuthorizationInterceptor;
	private final MemberPassportAuthorizationInterceptor memberPassportAuthorizationInterceptor;
	private final TraceLoggingInterceptor traceLoggingInterceptor;
	private final PassportUserArgumentResolver passportUserArgumentResolver;

	public WebConfiguration(
		GuestPassportAuthorizationInterceptor guestPassportAuthorizationInterceptor,
		MemberPassportAuthorizationInterceptor memberPassportAuthorizationInterceptor,
		TraceLoggingInterceptor traceLoggingInterceptor,
		PassportUserArgumentResolver passportUserArgumentResolver
	) {
		this.guestPassportAuthorizationInterceptor = guestPassportAuthorizationInterceptor;
		this.memberPassportAuthorizationInterceptor = memberPassportAuthorizationInterceptor;
		this.traceLoggingInterceptor = traceLoggingInterceptor;
		this.passportUserArgumentResolver = passportUserArgumentResolver;
	}

	/**
	 * 현재 개발된 API V1 에서는 guest 만 존재하기 때문에
	 * 모든 api path 에 대해 GuestPassportAuthorizationInterceptor 만 적용 <br>
	 * 인증된 Member 만 접근 가능한 api 가 개발되면 추가할 예정
	 *
	 * @param registry GuestPassportAuthorizationInterceptor
	 * @see GuestPassportAuthorizationInterceptor
	 * @see MemberPassportAuthorizationInterceptor
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(guestPassportAuthorizationInterceptor)
			.addPathPatterns("/api/v1/home/**")
			.addPathPatterns("/api/v1/legislation-accounts/**")
			.addPathPatterns("/api/v1/menu/**")
			.addPathPatterns("/api/v1/recent-bill/**")
			.excludePathPatterns("/")
			.excludePathPatterns("/health")
			.excludePathPatterns("/api/v1/auth/login")
			.excludePathPatterns("/api/v1/auth/reissue");
		registry.addInterceptor(traceLoggingInterceptor)
			.addPathPatterns("/api/v1/home/**")
			.addPathPatterns("/api/v1/legislation-accounts/**")
			.addPathPatterns("/api/v1/menu/**")
			.addPathPatterns("/api/v1/recent-bill/**")
			.excludePathPatterns("/")
			.excludePathPatterns("/health")
			.excludePathPatterns("/api/v1/auth/login")
			.excludePathPatterns("/api/v1/auth/reissue");
	}

	@Override
	public void addArgumentResolvers(@NotNull List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(passportUserArgumentResolver);
	}
}
