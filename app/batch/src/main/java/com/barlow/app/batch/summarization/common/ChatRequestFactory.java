package com.barlow.app.batch.summarization.common;

import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.request.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatRequestFactory {

    private final ChatRequest.Builder summarizeChatBuilder;

    public ChatRequestFactory(ChatRequest.Builder summarizeChatBuilder) {
        this.summarizeChatBuilder = summarizeChatBuilder;
    }

    public ChatRequest backgroundSummaryRequestFrom(String context) {
        return summarizeChatBuilder.copy()
                .addMessage(ChatMessage.ofUser(context))
                .background(true)
                .build();
    }
}
