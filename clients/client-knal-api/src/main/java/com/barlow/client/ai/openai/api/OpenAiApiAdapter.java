package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.exception.OpenAiApiException;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import feign.RetryableException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class OpenAiApiAdapter implements OpenAiApiPort {

    private final OpenAiApi api;

    public OpenAiApiAdapter(OpenAiApi api) {
        this.api = api;
    }

    @Override
    public OpenAiResponse getResponse(ChatRequest request) {
        return handleRetryable(() -> api.getChat(request));
    }

    @Override
    public OpenAiResponse getResponseById(String responseId) {
        return handleRetryable(() -> api.getChat(responseId));
    }

    private OpenAiResponse handleRetryable(Supplier<OpenAiResponse> responseSupplier) {
        try{
            return responseSupplier.get();
        } catch (RetryableException e) {
            throw new OpenAiApiException(500, "retryable exception occurs");
        }
    }
}
