package com.barlow.services.notification.worker;

import java.io.IOException;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.ExponentialBackOff;

public class RetryBackOff implements BackOff {

	private final BackOff delegate;
	private final int maxRetries;

	private int currentRetryCount;
	private long currentInterval;

	public RetryBackOff(BackOff delegate, int maxRetries) {
		this.delegate = delegate;
		this.maxRetries = maxRetries;
		this.currentRetryCount = 0;
	}

	@Override
	public void reset() {
		try {
			delegate.reset();
		} catch (IOException e) {
			throw new NotificationSendException(e.getMessage());
		}
		currentRetryCount = 0;
		currentInterval = 0;
	}

	@Override
	public long nextBackOffMillis() {
		long next;
		try {
			next = delegate.nextBackOffMillis();
		} catch (IOException e) {
			throw new NotificationSendException(e.getMessage());
		}
		if (next != BackOff.STOP) {
			currentRetryCount++;
		}
		currentInterval = next;
		return next;
	}

	boolean isTerminated() {
		return currentRetryCount >= maxRetries
			|| currentInterval == BackOff.STOP;
	}

	void waitUntilInterval(long interval) {
		try {
			Thread.sleep(interval);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new NotificationSendException(String.format("알림 재시도 스레드 interrupted : {%s}", e.getMessage()));
		}
	}

	int getCurrentRetryCount() {
		return currentRetryCount;
	}

	static RetryBackOff createServerError() {
		ExponentialBackOff backOff = new ExponentialBackOff.Builder()
			.setInitialIntervalMillis(10_000)         // 10초
			.setMaxIntervalMillis(60_000)             // 최대 60초
			.setMaxElapsedTimeMillis(70_000)          // 약 10 + 20 + 40 = 70초 → 3회 가능
			.setMultiplier(2.0)                       // 2배씩 증가
			.setRandomizationFactor(0.5)              // ±50% 지터링
			.build();
		return new RetryBackOff(backOff, 3);
	}

	static RetryBackOff createQuotaExceededError() {
		ExponentialBackOff backOff = new ExponentialBackOff.Builder()
			.setInitialIntervalMillis(60_000)         // 60초
			.setMaxIntervalMillis(60_000)             // 최대 60초
			.setMaxElapsedTimeMillis(61_000)          // 여유 1초 포함
			.build();
		return new RetryBackOff(backOff, 1);
	}
}
