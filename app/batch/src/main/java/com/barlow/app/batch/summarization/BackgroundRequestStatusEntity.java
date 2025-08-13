package com.barlow.app.batch.summarization;

import com.barlow.app.batch.summarization.common.SummaryRequestStatus;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record BackgroundRequestStatusEntity(
        LocalDate createdAt,
        List<SummaryRequestStatus> statuses
) implements Serializable {
}
