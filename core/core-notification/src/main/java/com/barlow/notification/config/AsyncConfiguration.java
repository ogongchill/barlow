package com.barlow.notification.config;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

	@Primary
	@Bean(name = "asyncThreadPoolExecutor")
	public Executor executor() {
		ThreadPoolTaskExecutor threadPoolTask = new ThreadPoolTaskExecutor();
		threadPoolTask.setCorePoolSize(10);
		threadPoolTask.setMaxPoolSize(50);
		threadPoolTask.setQueueCapacity(10);
		threadPoolTask.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTask.setAwaitTerminationSeconds(10);
		threadPoolTask.setThreadNamePrefix("notify-async-");
		threadPoolTask.initialize();
		return threadPoolTask;
	}
}
