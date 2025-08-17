package com.barlow.client.ai.openai.api;

import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "testClient",
        url = "https://api.openai.com/v1/responses",
        configuration = TestOpenAiFeignConfig.class
)
public interface TestClient {

    @PostMapping
    OpenAiResponse getChat(@RequestBody ChatRequest chatCompletionRequest);

    @GetMapping({"/{responseId}"})
    OpenAiResponse getChat(@PathVariable("responseId") String responseId);
}
