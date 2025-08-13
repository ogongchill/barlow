package com.barlow.app.batch.summarization.common;

import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;
import com.barlow.app.batch.summarization.AsyncSummaryRequestWorker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class OpenAiRequestExecutor {

    @Qualifier("workerSize")
    private final Integer workerSize;
    private final AsyncSummaryRequestWorker worker;

    private OpenAiRequestExecutor(Integer workerSize, AsyncSummaryRequestWorker worker) {
        this.workerSize = workerSize;
        this.worker = worker;
    }

    public List<SummaryRequestStatus> requestAsync(TodayBillInfoBatchEntity todayBillInfoBatchEntity) {
        LinkedBlockingQueue<BillAiRequestTask> taskQueue = todayBillInfoBatchEntity.items()
                .stream()
                .map(BillAiRequestTask::fromBillInfoItem)
                .collect(Collectors.toCollection(LinkedBlockingQueue::new));
        List<CompletableFuture<SummaryRequestStatus>> futures = createCompletableFutureFrom(taskQueue);
        repeat(workerSize, () -> worker.runAsync(taskQueue));
        List<SummaryRequestStatus> result = waitUntilDone(futures);
        repeat(workerSize, () -> worker.putPoisonPill(taskQueue)); // 요청 완료후 worker 종료
        return result;
    }

    private List<SummaryRequestStatus> waitUntilDone(List<CompletableFuture<SummaryRequestStatus>> futures) {
        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .filter(Objects::nonNull)
                        .toList())
                .join();
    }

    private List<CompletableFuture<SummaryRequestStatus>> createCompletableFutureFrom(LinkedBlockingQueue<BillAiRequestTask> taskQueue) {
        return taskQueue.stream()
                        .map(task -> task.summaryRequestStatusFuture()
                                .exceptionally(ex -> null))
                        .toList();
    }

    private void repeat(int workerSize, Runnable runnable) {
        for(int i = 0; i < workerSize; i ++) {
            runnable.run();
        }
    }
}
