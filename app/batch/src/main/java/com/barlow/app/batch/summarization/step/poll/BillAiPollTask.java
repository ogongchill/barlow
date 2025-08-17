package com.barlow.app.batch.summarization.step.poll;

import com.barlow.app.batch.summarization.common.BillAiSummary;
import com.barlow.app.batch.summarization.common.SummaryRequestStatus;
import com.barlow.client.ai.openai.api.common.OpenAiResponseStatus;

import java.util.concurrent.CompletableFuture;

public record BillAiPollTask(
        SummaryRequestStatus status,
        CompletableFuture<BillAiSummary> billAiSummaryFuture
) {

    public static final String POISON_PILL_BILL_ID = "POISON_PILL";

    public static BillAiPollTask ofPoisonPill() {
        return new BillAiPollTask(
                new SummaryRequestStatus(
                        POISON_PILL_BILL_ID,
                        "",
                        OpenAiResponseStatus.INCOMPLETE
                ),
                CompletableFuture.completedFuture(null)
        );
    }

    public boolean isPoisonPIll() {
        return status.billId().equals(POISON_PILL_BILL_ID);
    }

    @Override
    public String toString() {
        return "BillAiPollTask{" +
               "billId=" + status.billId() +
               "responseId=" + status.responseId() +
               '}';
    }

    public static BillAiPollTask fromSummaryStatus(SummaryRequestStatus summaryRequestStatus) {
        return new BillAiPollTask(
                summaryRequestStatus,
                new CompletableFuture<>()
        );
    }
}
