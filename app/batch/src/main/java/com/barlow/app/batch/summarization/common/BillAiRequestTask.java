package com.barlow.app.batch.summarization.common;

import com.barlow.app.batch.recentbill.job.TodayBillInfoBatchEntity;

import java.util.concurrent.CompletableFuture;

public record BillAiRequestTask(
        TodayBillInfoBatchEntity.BillInfoItem billInfoItem,
        CompletableFuture<SummaryRequestStatus> summaryRequestStatusFuture
) {
    private static final String POISON_PILL_BILL_ID = "";

    public static BillAiRequestTask fromBillInfoItem(TodayBillInfoBatchEntity.BillInfoItem billInfoItem) {
        return new BillAiRequestTask(billInfoItem, new CompletableFuture<>());
    }

    public boolean isPoisonPill() {
        return billInfoItem.billId().equals(POISON_PILL_BILL_ID);
    }

    public static BillAiRequestTask ofPoisonPill() {
        return new BillAiRequestTask(
                TodayBillInfoBatchEntity.BillInfoItem.builder()
                        .billId(POISON_PILL_BILL_ID)
                        .build(),
                CompletableFuture.completedFuture(null)
        );
    }

    @Override
    public String toString() {
        return "BillAiRequestTask{" +
               "billInfoItem=" + billInfoItem.billId() +
               billInfoItem.billName() +
               summaryRequestStatusFuture +
               '}';
    }
}
