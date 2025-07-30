package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;

public interface OpenAiApiPort {

    OpenAiResponse getResponse(ChatRequest request);
}
