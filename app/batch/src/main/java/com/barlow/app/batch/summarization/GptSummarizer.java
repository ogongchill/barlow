package com.barlow.app.batch.summarization;

import com.barlow.client.ai.openai.api.OpenAiApiPort;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.springframework.stereotype.Component;

@Component
public class GptSummarizer implements TextSummarizeClient {

    private final OpenAiApiPort api;
    private final ChatRequestFactory requestFactory;

    public GptSummarizer(OpenAiApiPort api, ChatRequestFactory requestFactory) {
        this.api = api;
        this.requestFactory = requestFactory;
    }

    @Override
    public String summarize(String text) {
        ChatRequest request = requestFactory.summarizeRequestFrom(text);
        OpenAiResponse response = api.getResponse(request);
        return response.output()
                .getFirst()
                .content()
                .getFirst()
                .text();
    }
}
