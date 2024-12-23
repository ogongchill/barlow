package com.barlow.notification.worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

public class FailureMessageRetryQueue {

	private static final int DELAY_IN_MILLIS = 60_000;
	private static final int MAX_RETRY_COUNT = 1;

	private final DelayQueue<RetryBatchMessages> retryQueue;

	public FailureMessageRetryQueue() {
		this.retryQueue = new DelayQueue<>();
	}

	public void pushAll(Map<Message, FirebaseMessagingException> failMessageWithException) {
		List<Message> retryableMessages = failMessageWithException.entrySet()
			.stream()
			.filter(entry -> entry.getValue().getMessagingErrorCode().equals(MessagingErrorCode.INTERNAL)
				|| entry.getValue().getMessagingErrorCode().equals(MessagingErrorCode.UNAVAILABLE)
				|| entry.getValue().getMessagingErrorCode().equals(MessagingErrorCode.QUOTA_EXCEEDED))
			.map(Map.Entry::getKey)
			.toList();
		retryQueue.put(new RetryBatchMessages(retryableMessages, DELAY_IN_MILLIS, MAX_RETRY_COUNT));
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

		public List<Message> getRetryMessages() {
			return retryMessages;
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
		public int compareTo(Delayed other) {
			if (other instanceof RetryBatchMessages retryBatchMessages) {
				return Long.compare(this.delayUntil, retryBatchMessages.delayUntil);
			}
			return 0;
		}
	}
}
