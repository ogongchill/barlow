package com.barlow.notification.worker;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;

class NotificationResultTest {

	private final Map<Message, MessagingErrorCode> failedMessages = Map.of(
		Message.builder().setToken("test").build(), MessagingErrorCode.THIRD_PARTY_AUTH_ERROR,
		Message.builder().setToken("test").build(), MessagingErrorCode.INVALID_ARGUMENT,
		Message.builder().setToken("test").build(), MessagingErrorCode.INTERNAL,
		Message.builder().setToken("test").build(), MessagingErrorCode.QUOTA_EXCEEDED,
		Message.builder().setToken("test").build(), MessagingErrorCode.SENDER_ID_MISMATCH,
		Message.builder().setToken("test").build(), MessagingErrorCode.UNAVAILABLE,
		Message.builder().setToken("test").build(), MessagingErrorCode.UNREGISTERED
	);
	private final NotificationResult result = new NotificationResult(failedMessages);

	@DisplayName("재시도 가능한 ErrorCode 들에 대한 메시지들을 반환한다")
	@Test
	void getRetryableMessages() {
		List<Message> retryableMessages = result.getRetryableMessages();

		assertThat(retryableMessages).hasSize(3);
	}

	@DisplayName("재시도 불가능한 ErrorCode 들에 대한 메시지들을 반환한다")
	@Test
	void getNonRetryableMessages() {
		List<Message> retryableMessages = result.getNonRetryableMessages();

		assertThat(retryableMessages).hasSize(4);
	}

	@DisplayName("재시도 가능한 ErrorCode 를 포함하고 있으면 true 를 반환한다")
	@Test
	void hasRetryableFailure_true() {
		boolean actual = result.hasRetryableFailure();

		assertThat(actual).isTrue();
	}


	@DisplayName("재시도 가능한 ErrorCode 를 포함하고 있지 않으면 false 를 반환한다")
	@Test
	void hasRetryableFailure_false() {
		NotificationResult givenResult = new NotificationResult(Map.of(
			Message.builder().setToken("test").build(), MessagingErrorCode.THIRD_PARTY_AUTH_ERROR,
			Message.builder().setToken("test").build(), MessagingErrorCode.INVALID_ARGUMENT,
			Message.builder().setToken("test").build(), MessagingErrorCode.SENDER_ID_MISMATCH,
			Message.builder().setToken("test").build(), MessagingErrorCode.UNREGISTERED
		));
		boolean actual = givenResult.hasRetryableFailure();

		assertThat(actual).isFalse();
	}
}