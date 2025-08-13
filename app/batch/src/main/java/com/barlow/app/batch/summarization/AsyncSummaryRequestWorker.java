package com.barlow.app.batch.summarization;

import com.barlow.app.batch.summarization.common.BillAiRequestTask;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.client.ai.openai.api.OpenAiApiPort;
import com.barlow.client.ai.openai.api.exception.OpenAiApiException;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class AsyncSummaryRequestWorker {

    private static final Map<BillAiRequestTask, AtomicInteger> RETRY_COUNT_MAP = new ConcurrentHashMap<>();
    private static final int MAX_RETRY_COUNT = 2;
    private static final Logger log = LoggerFactory.getLogger(AsyncSummaryRequestWorker.class);
    private final OpenAiApiPort api;
    private final ChatRequestFactory chatRequestFactory;

    public AsyncSummaryRequestWorker(OpenAiApiPort api, ChatRequestFactory chatRequestFactory) {
        this.api = api;
        this.chatRequestFactory = chatRequestFactory;
    }

    @Async("summaryExecutor")
    public void runAsync(LinkedBlockingQueue<BillAiRequestTask> taskQueue) {
        BillAiRequestTask task = null;
        while(true) {
            try {
                task = taskQueue.take();
                if(task.isPoisonPill()) {
                    break;
                }
                processOpenAiRequest(task);
            } catch (OpenAiApiException e) {
                log.error(e.getMessage());
                if(task != null) {
                    retry(task, taskQueue);
                }
            } catch (InterruptedException e) {
                log.error("{}가 take() 도중 인터럽트되어 종료합니다", Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processOpenAiRequest(BillAiRequestTask task) throws OpenAiApiException {
        if(task.billInfoItem().summary() == null) {
            task.summaryRequestStatusFuture().complete(null);
            return;
        }
        ChatRequest request = chatRequestFactory.backgroundSummaryRequestFrom(task.billInfoItem().summary());
        OpenAiResponse response = api.getResponse(request);
        SummaryRequestStatus status = new SummaryRequestStatus(task.billInfoItem().billId(), response.id(), response.status());
        task.summaryRequestStatusFuture().complete(status);
    }

    private void retry(BillAiRequestTask task, LinkedBlockingQueue<BillAiRequestTask> taskQueue) {
        try {
            AtomicInteger retryCount = RETRY_COUNT_MAP.getOrDefault(task, new AtomicInteger(0));
            if (retryCount.get() < MAX_RETRY_COUNT) {
                retryCount.incrementAndGet();
                taskQueue.put(task); // 재시도를 위해 큐에 삽입
                log.info("{}를 재시도 위해 다시 taskQueue 삽입 (시도 횟수: {})", task, retryCount.get());
                RETRY_COUNT_MAP.put(task, retryCount);  // retryCount 갱신
            } else {
                log.info("{}는 최대 재시도 횟수에 도달하여 재시도하지 않습니다.", task);
            }
        } catch (InterruptedException e) {
            log.error("{}가 retry 도중 인터럽트되어 종료합니다", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
    }

    @Async("summaryExecutor")
    public void putPoisonPill(LinkedBlockingQueue<BillAiRequestTask> taskQueue) {
        try {
            taskQueue.put(BillAiRequestTask.ofPoisonPill());
        } catch (InterruptedException e) {
            log.error("{}가 POISON_PILL put() 도중 인터럽트되어 종료합니다", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
    }
}
