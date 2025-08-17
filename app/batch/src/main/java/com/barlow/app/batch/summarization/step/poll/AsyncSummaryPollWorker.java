package com.barlow.app.batch.summarization.step.poll;

import com.barlow.app.batch.summarization.common.BillAiSummary;
import com.barlow.client.ai.openai.api.OpenAiApiPort;
import com.barlow.client.ai.openai.api.common.OpenAiResponseStatus;
import com.barlow.client.ai.openai.api.exception.OpenAiException;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.LinkedBlockingDeque;

@Component
public class AsyncSummaryPollWorker {

    private static final Logger log = LoggerFactory.getLogger(AsyncSummaryPollWorker.class);
    private static final long RETRY_TIME_THRESHOLD = Duration.ofSeconds(60).toNanos();
    private final OpenAiApiPort api;

    public AsyncSummaryPollWorker(OpenAiApiPort api) {
        this.api = api;
    }

    @Async("summaryExecutor")
    public void runAsync(LinkedBlockingDeque<BillAiPollTask> pollTaskQueue) {
        long deadline = System.nanoTime() + RETRY_TIME_THRESHOLD;
        BillAiPollTask pollTask = null;
        while(true) {
            try {
                pollTask = pollTaskQueue.take();
                if(pollTask.isPoisonPIll()) {
                    break;
                }
                OpenAiResponse response = api.getResponseById(pollTask.status().responseId());
                switch (response.status()) {
                    case OpenAiResponseStatus.COMPLETED -> complete(pollTask, response);
                    case OpenAiResponseStatus.IN_PROGRESS, OpenAiResponseStatus.QUEUED -> retry(pollTask, pollTaskQueue, deadline);
                    default -> completeAsNull(pollTask);
                }
            } catch (InterruptedException e) {
                log.info("{}가 인터럽트 되어 종료됩니다", pollTask);
                Thread.currentThread().interrupt();
            } catch (OpenAiException e) {
                retry(pollTask, pollTaskQueue, deadline);
            }
        }
        log.info("{} 종료", Thread.currentThread().getName());
    }

    private void complete(BillAiPollTask pollTask, OpenAiResponse response) {
        pollTask.billAiSummaryFuture().complete(
                new BillAiSummary(
                        pollTask.status().billId(),
                        response.output().getFirst().content().getFirst().text()
                )
        );
    }

    private void completeAsNull(BillAiPollTask task) {
        task.billAiSummaryFuture().complete(null);
    }

    private void retry(BillAiPollTask task, LinkedBlockingDeque<BillAiPollTask> pollTaskQueue, long deadline) {
        try {
            if(pollTaskQueue.isEmpty()) {
                Thread.sleep(1000L); // 현재 작업이 하나만 남았다면 1초에 한번씩 요청
            }
            if(System.nanoTime() < deadline) {
                pollTaskQueue.put(task);
                return;
            }
            task.billAiSummaryFuture().complete(null);
        } catch (InterruptedException e) {
            log.info("{}가 retry 도중 인터럽트 되어 종료됩니다", task);
            Thread.currentThread().interrupt();
        }
    }
}
