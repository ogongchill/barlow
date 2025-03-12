package com.barlow.core.api.config;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.barlow.support.alert.ErrorAlerter;

@EnableAsync
@Configuration
public class CoreApiAsyncConfiguration implements AsyncConfigurer {

	private final ErrorAlerter errorAlerter;

	public CoreApiAsyncConfiguration(ErrorAlerter errorAlerter) {
		this.errorAlerter = errorAlerter;
	}

	@Override
	@Bean("coreAsyncUncaughtExceptionHandler")
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncExceptionHandler(errorAlerter);
	}

	@Override
	@Bean("asyncExecutor")
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor threadPoolTask = new ThreadPoolTaskExecutor();
		threadPoolTask.setCorePoolSize(10);
		threadPoolTask.setMaxPoolSize(50);
		threadPoolTask.setQueueCapacity(10);
		threadPoolTask.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTask.setAwaitTerminationSeconds(10);
		threadPoolTask.setThreadNamePrefix("async-");
		threadPoolTask.initialize();
		return threadPoolTask;
	}
}
