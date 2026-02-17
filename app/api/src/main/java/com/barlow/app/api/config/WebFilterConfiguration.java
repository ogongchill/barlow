package com.barlow.app.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.barlow.services.auth.support.filter.FilterExceptionHandler;
import com.barlow.services.auth.support.filter.InboundJwtAuthenticationFilter;

@Configuration
public class WebFilterConfiguration {

	private final HandlerValidationFilter handlerValidationFilter;
	private final FilterExceptionHandler filterExceptionHandler;
	private final InboundJwtAuthenticationFilter inboundJwtAuthenticationFilter;

	public WebFilterConfiguration(
		HandlerValidationFilter handlerValidationFilter,
		FilterExceptionHandler filterExceptionHandler,
		InboundJwtAuthenticationFilter inboundJwtAuthenticationFilter
	) {
		this.handlerValidationFilter = handlerValidationFilter;
		this.filterExceptionHandler = filterExceptionHandler;
		this.inboundJwtAuthenticationFilter = inboundJwtAuthenticationFilter;
	}

	@Bean
	public FilterRegistrationBean<HandlerValidationFilter> handlerValidationFilterBean() {
		FilterRegistrationBean<HandlerValidationFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(handlerValidationFilter);
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<FilterExceptionHandler> filterExceptionHandlerBean() {
		FilterRegistrationBean<FilterExceptionHandler> bean = new FilterRegistrationBean<>();
		bean.setFilter(filterExceptionHandler);
		bean.addUrlPatterns("/*");
		bean.setOrder(2);
		return bean;
	}

	@Bean
	public FilterRegistrationBean<InboundJwtAuthenticationFilter> filterRegistrationBean() {
		FilterRegistrationBean<InboundJwtAuthenticationFilter> bean = new FilterRegistrationBean<>();
		bean.setFilter(inboundJwtAuthenticationFilter);
		bean.addUrlPatterns("/*");
		bean.setOrder(3);
		return bean;
	}
}
