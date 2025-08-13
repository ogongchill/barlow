package com.barlow.app.batch.summarization.common;

import com.barlow.client.ai.openai.api.common.OpenAiResponseStatus;

public record SummaryRequestStatus(
        String billId,
        String responseId,
        OpenAiResponseStatus responseStatus
) {
}
