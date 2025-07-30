package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.springframework.stereotype.Component;

@Component
public class OpenAiApiAdapter implements OpenAiApiPort {

    private final OpenAiApi api;

    public OpenAiApiAdapter(OpenAiApi api) {
        this.api = api;
    }

    @Override
    public OpenAiResponse getResponse(ChatRequest request) {
        return api.getChat(request);
    }
}
