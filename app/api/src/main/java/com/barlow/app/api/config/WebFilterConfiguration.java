package com.barlow.app.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.barlow.services.support.filter.InboundJwtAuthenticationFilter;

@Configuration
public class WebFilterConfiguration {

	private final InboundJwtAuthenticationFilter inboundJwtAuthenticationFilter;

	public WebFilterConfiguration(InboundJwtAuthenticationFilter inboundJwtAuthenticationFilter) {
		this.inboundJwtAuthenticationFilter = inboundJwtAuthenticationFilter;
	}

	@Bean
	public FilterRegistrationBean<InboundJwtAuthenticationFilter> filterRegistrationBean() {
		FilterRegistrationBean<InboundJwtAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		filterRegistrationBean.setFilter(inboundJwtAuthenticationFilter);
		filterRegistrationBean.addUrlPatterns("/*");
		return filterRegistrationBean;
	}
}
