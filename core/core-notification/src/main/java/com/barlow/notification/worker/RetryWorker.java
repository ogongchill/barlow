package com.barlow.notification.worker;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetryWorker {

	private static final Logger log = LoggerFactory.getLogger(RetryWorker.class);
	private static final FailureMessageRetryQueue FAILURE_MESSAGE_RETRY_QUEUE
		= new FailureMessageRetryQueue(60_000, 1);

	private final Executor executor;
	private final NotificationSender notificationSender;

	public RetryWorker(Executor executor, NotificationSender notificationSender) {
		this.executor = executor;
		this.notificationSender = notificationSender;
	}

	public void start(NotificationResult result) {
		FAILURE_MESSAGE_RETRY_QUEUE.pushAllRetryableMessages(result.getRetryableMessages());
		executor.execute(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					FailureMessageRetryQueue.RetryBatchMessages retryBatchMessages = FAILURE_MESSAGE_RETRY_QUEUE.take();
					if (retryBatchMessages.getRetryCount() > 0) {
						retryBatchMessages.decrementRetryCount();
						log.info("실패 메시지 {}개 재전송...{}", retryBatchMessages.size(), retryBatchMessages.getMessages());
						NotificationResult retryResult = notificationSender.send(retryBatchMessages.getMessages());

						log.info("남은 재시도 횟수 {}. 실패 메시지 재시도 큐 clear", retryBatchMessages.getRetryCount());
						FAILURE_MESSAGE_RETRY_QUEUE.clear();

						handleRetryResult(retryBatchMessages, retryResult);
					} else {
						FAILURE_MESSAGE_RETRY_QUEUE.clear();
						Thread.currentThread().interrupt();
						log.info("재시도 횟수 초과. 실패 메시지 재시도 큐 clear");
					}
				} catch (InterruptedException e) {
					log.warn("알림 재전송 쓰레드 - {} 에서 문제 발생 : {}", Thread.currentThread().getName(), e.getMessage(), e);
					FAILURE_MESSAGE_RETRY_QUEUE.clear();
					Thread.currentThread().interrupt();
					break;
				}
			}
		});
	}

	private void handleRetryResult(
		FailureMessageRetryQueue.RetryBatchMessages retryBatchMessages,
		NotificationResult retryResult
	) {
		logNonRetryableMessages(retryResult);
		if (retryBatchMessages.getRetryCount() != 0 && retryResult.hasRetryableFailure()) {
			FAILURE_MESSAGE_RETRY_QUEUE.pushAllRetryableMessages(retryResult.getRetryableMessages());
		} else if (retryBatchMessages.getRetryCount() == 0 && retryResult.hasRetryableFailure()) {
			log.info("재시도 가능한 dead letter 발생 : {}", retryResult.getRetryableMessages());
			FAILURE_MESSAGE_RETRY_QUEUE.clear();
		} else {
			Thread.currentThread().interrupt();
			FAILURE_MESSAGE_RETRY_QUEUE.clear();
			log.info("재시도 완료 후 RetryWorker 종료");
		}
	}

	private void logNonRetryableMessages(NotificationResult result) {
		result.getNonRetryableMessages()
			.forEach(message -> log.info("재시도 불가능한 실패 메시지 : {}", message));
	}
}
