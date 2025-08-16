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

import java.util.concurrent.LinkedBlockingQueue;

@Component
public class AsyncSummaryPollWorker {

    private static final Logger log = LoggerFactory.getLogger(AsyncSummaryPollWorker.class);
    private final OpenAiApiPort api;

    public AsyncSummaryPollWorker(OpenAiApiPort api) {
        this.api = api;
    }

    @Async("summaryExecutor")
    public void runAsync(LinkedBlockingQueue<BillAiPollTask> pollTaskQueue) {
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
                    case OpenAiResponseStatus.IN_PROGRESS, OpenAiResponseStatus.QUEUED -> retry(pollTask, pollTaskQueue);
                    default -> completeAsNull(pollTask);
                }
            } catch (InterruptedException e) {
                log.info("{}가 인터럽트 되어 종료됩니다", pollTask);
                Thread.currentThread().interrupt();
            } catch (OpenAiException e) {
                retry(pollTask, pollTaskQueue);
            }
        }
        log.info("{} 종료", Thread.currentThread().getName());
    }

    public void putPoisonPill(LinkedBlockingQueue<BillAiPollTask> pollTaskQueue) {
        try {
            pollTaskQueue.put(BillAiPollTask.ofPoisonPill());
        } catch (InterruptedException e) {
            log.error("{}가 POISON_PILL put() 도중 인터럽트되어 종료합니다", Thread.currentThread().getName());
            Thread.currentThread().interrupt();
        }
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

    private void retry(BillAiPollTask task, LinkedBlockingQueue<BillAiPollTask> pollTaskQueue) {
        try {
            if(pollTaskQueue.isEmpty()) {
                Thread.sleep(1000L); // 현재 작업이 하나만 남았다면 1초에 한번씩 요청
            }
            pollTaskQueue.put(task);
        } catch (InterruptedException e) {
            log.info("{}가 retry 도중 인터럽트 되어 종료됩니다", task);
            Thread.currentThread().interrupt();
        }
    }
}
