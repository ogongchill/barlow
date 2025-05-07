package com.barlow.services.notification.worker;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.google.firebase.messaging.Message;

class FailureMessageRetryQueueTest {

	private FailureMessageRetryQueue testRetryQueue;
	private List<Message> retryableMessages;

	@BeforeEach
	void setUp() {
		testRetryQueue = new FailureMessageRetryQueue(0, 1);
		retryableMessages = List.of(
			Message.builder().setToken("test").build(),
			Message.builder().setToken("test").build(),
			Message.builder().setToken("test").build()
		);
	}

	@DisplayName("재시도 가능한 실패 메시지들을 queue 에 삽입한다")
	@Test
	void pushAllRetryableMessages() throws InterruptedException {
		testRetryQueue.pushAllRetryableMessages(retryableMessages);

		assertThat(testRetryQueue.isEmpty()).isFalse();
		assertThat(testRetryQueue.take()).isInstanceOf(FailureMessageRetryQueue.RetryBatchMessages.class);
	}

	@DisplayName("실패 메시지 재시도 queue 를 비운다")
	@Test
	void clear() {
		testRetryQueue.pushAllRetryableMessages(retryableMessages);

		testRetryQueue.clear();

		assertThat(testRetryQueue.isEmpty()).isTrue();
	}
}