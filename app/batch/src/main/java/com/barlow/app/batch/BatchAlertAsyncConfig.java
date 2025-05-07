package com.barlow.app.batch;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class BatchAlertAsyncConfig {

	@Primary
	@Bean(name = "asyncExecutor")
	public Executor executor() {
		ThreadPoolTaskExecutor threadPoolTask = new ThreadPoolTaskExecutor();
		threadPoolTask.setCorePoolSize(1);
		threadPoolTask.setMaxPoolSize(1);
		threadPoolTask.setQueueCapacity(10);
		threadPoolTask.setThreadNamePrefix("alert-async-");
		threadPoolTask.setWaitForTasksToCompleteOnShutdown(true);
		threadPoolTask.setAwaitTerminationSeconds(5);
		threadPoolTask.initialize();
		return threadPoolTask;
	}
}
