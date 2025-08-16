package com.barlow.app.batch.summarization.step.poll;

import com.barlow.app.batch.summarization.common.BackgroundRequestStatusEntity;
import com.barlow.app.batch.summarization.common.BillAiSummary;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class BillAiSummaryPollExecutor {

    @Qualifier("workerSize")
    private final Integer workerSize;
    private final AsyncSummaryPollWorker worker;

    public BillAiSummaryPollExecutor(Integer workerSize, AsyncSummaryPollWorker worker) {
        this.workerSize = workerSize;
        this.worker = worker;
    }

    public List<BillAiSummary> requestAsync(BackgroundRequestStatusEntity entity) {
        LinkedBlockingQueue<BillAiPollTask> taskQueue = entity.statuses()
                .stream()
                .map(BillAiPollTask::fromSummaryStatus)
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        List<CompletableFuture<BillAiSummary>> futures = taskQueue.stream()
                .map(task -> task.billAiSummaryFuture()
                        .exceptionally(e -> {
                            e.printStackTrace();
                            return null;
                        }))
                .toList();
        repeatWorkerSize(() -> worker.runAsync(taskQueue));
        List<BillAiSummary> result = waitUntilDone(futures);
        repeatWorkerSize(() -> worker.putPoisonPill(taskQueue));
        return result;
    }

    private void repeatWorkerSize(Runnable runnable) {
        for(int i = 0; i < workerSize; i ++) {
            runnable.run();
        }
    }

    private List<BillAiSummary> waitUntilDone(List<CompletableFuture<BillAiSummary>> futures) {
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .toList())
                .join();
    }
}
