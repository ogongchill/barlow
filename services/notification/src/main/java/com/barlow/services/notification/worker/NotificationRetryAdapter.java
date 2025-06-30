package com.barlow.services.notification.worker;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.barlow.core.enumerate.DeviceOs;
import com.google.firebase.messaging.MessagingErrorCode;

@Component
public class NotificationRetryAdapter implements NotificationRetryPort {

	private static final Logger log = LoggerFactory.getLogger(NotificationRetryAdapter.class);

	private final Map<DeviceOs, NotificationSender> senders;
	private final Map<MessagingErrorCode, RetryBackOff> retryBackOffs;
	private final Executor executor;
	private final IdempotencyRepository idempotencyRepository;

	public NotificationRetryAdapter(
		List<NotificationSender> senders,
		@Qualifier("asyncThreadPoolExecutor") Executor executor,
		IdempotencyRepository idempotencyRepository
	) {
		this.senders = senders.stream().collect(Collectors.toMap(NotificationSender::supportedOs, s -> s));
		this.retryBackOffs = Map.of(
			MessagingErrorCode.INTERNAL, RetryBackOff.createServerError(),
			MessagingErrorCode.UNAVAILABLE, RetryBackOff.createServerError(),
			MessagingErrorCode.QUOTA_EXCEEDED, RetryBackOff.createQuotaExceededError()
		);
		this.idempotencyRepository = idempotencyRepository;
		this.executor = executor;
	}

	@Override
	public void retry(RetryMessage retryMessage) {
		NotificationSender sender = senders.get(retryMessage.os());
		for (Map.Entry<MessagingErrorCode, List<NotificationMessage>> entry
			: retryMessage.retryableMessages().entrySet()
		) {
			CompletableFuture.runAsync(() -> start(entry, sender), executor);
		}
	}

	private void start(Map.Entry<MessagingErrorCode, List<NotificationMessage>> entry, NotificationSender sender) {
		RetryBackOff backOff = retryBackOffs.get(entry.getKey());
		List<NotificationMessage> retryableMessages = entry.getValue().stream()
			.filter(msg -> idempotencyRepository.checkFailed(msg.id()))
			.toList();

		while (!backOff.isTerminated() && !retryableMessages.isEmpty()) {
			backOff.waitUntilInterval(backOff.nextBackOffMillis());
			NotificationResult retryResult = sender.send(retryableMessages);
			retryResult.getSuccessMessages().forEach(idempotencyRepository::markAsSuccess);

			if (retryResult.failureResult().isEmpty()) {
				log.info("[재시도 {}회] 모든 실패 메시지 재전송 성공.\nRetryWorker 종료", backOff.getCurrentRetryCount());
				break;
			} else if (retryResult.hasRetryableFailure()) {
				retryableMessages = retryResult.getRetryableMessages().stream()
					.filter(msg -> idempotencyRepository.checkFailed(msg.id()))
					.toList();
			} else {
				retryResult.getNonRetryableMessages()
					.forEach(msg -> log.info("[재시도 {}회] 재시도 불가능한 실패 메시지 : {}", backOff.getCurrentRetryCount(), msg));
			}
		}
		backOff.reset();

		if (!retryableMessages.isEmpty()) {
			retryableMessages.forEach(msg -> log.info("[재시도 종료] 재시도 가능한 dead letter 발생 : {}", msg));
		}
	}
}
