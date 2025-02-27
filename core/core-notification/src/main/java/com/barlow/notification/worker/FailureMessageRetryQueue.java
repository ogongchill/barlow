package com.barlow.notification.worker;

import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import com.google.firebase.messaging.Message;

public class FailureMessageRetryQueue {

	private final DelayQueue<RetryBatchMessages> retryQueue;
	private final int delayInMillis;
	private final int maxRetryCount;

	public FailureMessageRetryQueue(int delayInMillis, int maxRetryCount) {
		this.retryQueue = new DelayQueue<>();
		this.delayInMillis = delayInMillis;
		this.maxRetryCount = maxRetryCount;
	}

	public void pushAllRetryableMessages(List<Message> retryableMessages) {
		retryQueue.put(new RetryBatchMessages(retryableMessages, delayInMillis, maxRetryCount));
	}

	public RetryBatchMessages take() throws InterruptedException {
		return retryQueue.take();
	}

	public boolean isEmpty() {
		return retryQueue.isEmpty();
	}

	public void clear() {
		retryQueue.clear();
	}

	public static class RetryBatchMessages implements Delayed {

		private final List<Message> retryMessages;
		private final long delayUntil;
		private int retryCount;

		public RetryBatchMessages(List<Message> retryMessages, long delayInMillis, int retryCount) {
			this.retryMessages = retryMessages;
			this.delayUntil = System.currentTimeMillis() + delayInMillis;
			this.retryCount = retryCount;
		}

		public List<Message> getMessages() {
			return retryMessages;
		}

		public int size() {
			return retryMessages.size();
		}

		public int getRetryCount() {
			return retryCount;
		}

		public void decrementRetryCount() {
			retryCount--;
		}

		@Override
		public long getDelay(TimeUnit unit) {
			long delay = delayUntil - System.currentTimeMillis();
			return unit.convert(delay, TimeUnit.MILLISECONDS);
		}

		@Override
		public int compareTo(@NotNull Delayed other) {
			if (other instanceof RetryBatchMessages retryBatchMessages) {
				return Long.compare(this.delayUntil, retryBatchMessages.delayUntil);
			}
			return 0;
		}
	}
}
