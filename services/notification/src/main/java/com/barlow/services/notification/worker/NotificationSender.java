package com.barlow.services.notification.worker;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.barlow.core.enumerate.DeviceOs;
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

	public NotificationResult send(List<NotificationMessage> messages) {
		List<Message> fcmMessages = messages.stream()
			.map(NotificationMessage::message)
			.toList();
		ApiFuture<BatchResponse> batchResponseFuture = firebaseMessaging.sendEachAsync(fcmMessages);
		BatchResponse batchResponse = getBatchResponse(batchResponseFuture);
		List<SendResponse> sendResponses = batchResponse.getResponses();
		log.info("{} : 알림 전송 완료", LocalDateTime.now());
		return mappingNotificationResult(messages, sendResponses);
	}

	private BatchResponse getBatchResponse(ApiFuture<BatchResponse> batchResponseFuture) {
		try {
			return batchResponseFuture.get();
		} catch (InterruptedException e) {
			log.warn("알림 결과 조회 중 인터럽트됨: {} - 원인 : {}", e.getMessage(), e);
			Thread.currentThread().interrupt();
			throw new NotificationSendException("알림 쓰레드 인터럽트");
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			log.error("알림 결과 조회 중 문제 발생: {} - 원인: {}", cause.getClass().getSimpleName(), cause.getMessage(), cause);
			throw new NotificationSendException("알림 결과 수신 실패", cause);
		}
	}

	private NotificationResult mappingNotificationResult(
		List<NotificationMessage> messages,
		List<SendResponse> sendResponses
	) {
		Map<NotificationMessage, MessagingErrorCode> failedMessages = new HashMap<>();
		List<NotificationMessage> successMessages = new ArrayList<>();
		for (int i = 0; i < sendResponses.size(); i++) {
			SendResponse sr = sendResponses.get(i);
			NotificationMessage msg = messages.get(i); // 입력된 메시지 리스트와 결과 리스트는 순서가 일치함
			if (sr.isSuccessful()) {
				successMessages.add(msg);
			} else {
				failedMessages.put(msg, sr.getException().getMessagingErrorCode());
			}
		}
		return new NotificationResult(failedMessages, successMessages);
	}

	protected abstract DeviceOs supportedOs();
}
