package com.barlow.app.batch.summarization;

import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.request.ChatMessage;
import org.springframework.stereotype.Component;

@Component
public class ChatRequestFactory {

    private final ChatRequest.Builder summarizeChatBuilder;

    public ChatRequestFactory(ChatRequest.Builder summarizeChatBuilder) {
        this.summarizeChatBuilder = summarizeChatBuilder;
    }

    public ChatRequest summarizeRequestFrom(String context) {
        return summarizeChatBuilder.copy()
                .addMessage(ChatMessage.ofUser(context))
                .build();
    }
}
