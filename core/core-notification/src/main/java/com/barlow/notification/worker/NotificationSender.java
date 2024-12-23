package com.barlow.notification.worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.core.ApiFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.SendResponse;

public abstract class NotificationSender {

	private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);
	private static final FailureMessageRetryQueue FAILURE_MESSAGE_RETRY_QUEUE = new FailureMessageRetryQueue();

	protected final FirebaseMessaging firebaseMessaging;

	protected NotificationSender(FirebaseMessaging firebaseMessaging) {
		this.firebaseMessaging = firebaseMessaging;
		this.startRetryWorker();
	}

	protected void send(List<Message> messages) {
		ApiFuture<BatchResponse> batchResponseFuture = firebaseMessaging.sendEachAsync(messages);
		Runnable callback = createCallbackTask(batchResponseFuture, messages);
		batchResponseFuture.addListener(callback, MoreExecutors.directExecutor());
	}

	protected Runnable createCallbackTask(ApiFuture<BatchResponse> batchResponseFuture, List<Message> messages) {
		return () -> {
			try {
				BatchResponse batchResponse = batchResponseFuture.get();
				List<SendResponse> sendResponses = batchResponse.getResponses();
				Map<Message, FirebaseMessagingException> failMessageWithException = sendResponses.stream()
					.filter(sendResponse -> !sendResponse.isSuccessful())
					.collect(Collectors.toMap(
						sendResponse -> messages.get(sendResponses.indexOf(sendResponse)),
						SendResponse::getException
					));
				failMessageWithException.forEach((message, e) ->
					log.info("실패 message: {}. Reason: {}", message, e.getMessage(), e)
				);
				FAILURE_MESSAGE_RETRY_QUEUE.pushAll(failMessageWithException);
			} catch (InterruptedException e) {
				log.warn("알림 전송 쓰레드 - {} 에서 문제 발생 : {}", Thread.currentThread().getName(), e.getMessage(), e);
				Thread.currentThread().interrupt();
			} catch (ExecutionException e) {
				log.warn("알림 전송 쓰레드 - {} 에서 문제 발생 : {}", Thread.currentThread().getName(), e.getMessage(), e);
			}
		};
	}

	protected void startRetryWorker() {
		Runnable retryTask = () -> {
			while (true) {
				try {
					FailureMessageRetryQueue.RetryBatchMessages retryBatchMessages = FAILURE_MESSAGE_RETRY_QUEUE.take();
					if (retryBatchMessages.getRetryCount() > 0) {
						retryBatchMessages.decrementRetryCount();
						log.info("전송 실패 메시지 재전송...{}", retryBatchMessages.getRetryMessages());
						send(retryBatchMessages.getRetryMessages());
						log.info("남은 재시도 횟수 {}. 실패 메시지 재시도 큐 clear", retryBatchMessages.getRetryCount());
						FAILURE_MESSAGE_RETRY_QUEUE.clear();
					} else {
						FAILURE_MESSAGE_RETRY_QUEUE.clear();
						log.info("재시도 횟수 초과. 실패 메시지 재시도 큐 clear");
					}
				} catch (InterruptedException e) {
					log.warn("알림 재전송 쓰레드 - {} 에서 문제 발생 : {}", Thread.currentThread().getName(), e.getMessage(), e);
					Thread.currentThread().interrupt();
					break;
				}
			}
		};
		Thread retryWorker = new Thread(retryTask);
		retryWorker.setDaemon(true);
		retryWorker.start();
	}
}
