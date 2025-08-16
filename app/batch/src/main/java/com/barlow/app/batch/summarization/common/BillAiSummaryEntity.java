package com.barlow.app.batch.summarization.common;

import java.io.Serializable;
import java.util.List;

public record BillAiSummaryEntity(
        List<BillAiSummary> items
    ) implements Serializable {

    public String getTextById(String id) {
        return items.stream()
                .filter(summary -> summary.billId().equals(id))
                .findFirst()
                .map(BillAiSummary::text)
                .orElse(null);
    }
}
