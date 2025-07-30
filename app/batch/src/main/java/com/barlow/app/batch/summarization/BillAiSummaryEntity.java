package com.barlow.app.batch.summarization;

import java.io.Serializable;
import java.util.List;

public record BillAiSummaryEntity(
        List<BillAiSummary> items
    ) implements Serializable {

    public BillAiSummary findById(String id) {
        return items.stream()
                .filter(summary -> summary.billId().equals(id))
                .findFirst()
                .orElse(null);
    }
}
