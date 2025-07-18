package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.request.ChatCompletionRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "${ai.open-ai.api.name}",
        url = "${ai.open-ai.api.url}",
        configuration = OpenAiFeignConfig.class
)
public interface OpenAiClient {

    @PostMapping
    OpenAiResponse getChat(@RequestBody ChatCompletionRequest chatCompletionRequest);
}