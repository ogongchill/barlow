package com.barlow.notification.worker;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.api.core.ApiFuture;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.SendResponse;

public abstract class NotificationSender {

	private static final Logger log = LoggerFactory.getLogger(NotificationSender.class);

	protected final FirebaseMessaging firebaseMessaging;

	protected NotificationSender(FirebaseMessaging firebaseMessaging) {
		this.firebaseMessaging = firebaseMessaging;
	}

	protected NotificationResult send(List<Message> messages) {
		ApiFuture<BatchResponse> batchResponseFuture = firebaseMessaging.sendEachAsync(messages);
		BatchResponse batchResponse = getBatchResponse(batchResponseFuture);
		List<SendResponse> sendResponses = batchResponse.getResponses();
		Map<Message, MessagingErrorCode> failedMessages = extractFailedMessages(messages, sendResponses);
		if (!failedMessages.isEmpty()) {
			logFailedMessages(failedMessages);
			return new NotificationResult(failedMessages);
		} else {
			log.info("{} : 알림 전송 성공", LocalDateTime.now());
			return new NotificationResult(Map.of());
		}
	}

	private BatchResponse getBatchResponse(ApiFuture<BatchResponse> batchResponseFuture) {
		try {
			return batchResponseFuture.get();
		} catch (InterruptedException e) {
			log.warn("알림 결과 조회 중 인터럽트됨: {} - 원인 : {}", e.getMessage(), e);
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			log.error("알림 결과 조회 중 문제 발생: {} - 원인: {}", cause.getClass().getSimpleName(), cause.getMessage(), cause);
		}
		throw new IllegalStateException("커스텀 예외 만들어서 던질게~"); // todo : 예외처리 다시 하기
	}

	private Map<Message, MessagingErrorCode> extractFailedMessages(
		List<Message> messages,
		List<SendResponse> sendResponses
	) {
		return sendResponses.stream()
			.filter(sendResponse -> !sendResponse.isSuccessful())
			.collect(Collectors.toMap(
				sendResponse -> messages.get(sendResponses.indexOf(sendResponse)),
				sendResponse1 -> sendResponse1.getException().getMessagingErrorCode()
			));
	}

	private void logFailedMessages(Map<Message, MessagingErrorCode> failedMessages) {
		failedMessages.forEach((message, errorCode) ->
			log.info("실패 메시지: {}. 원인: {}", message, errorCode)
		);
	}
}
