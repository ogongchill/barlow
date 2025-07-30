package com.barlow.client.ai.openai.api;


import com.barlow.client.ai.openai.api.common.OpenAiModel;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.request.ChatMessage;
import com.barlow.client.ai.openai.api.response.OpenAiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestConfig.class)
@ActiveProfiles("test")
class OpenAiClientTest {

    @Autowired
    private TestClient testClient;

    @Test
    void testChatCompletionLive() {
        ChatRequest request = ChatRequest.builder()
                .model(OpenAiModel.GPT_4_1_MINI)
                .addMessage(new ChatMessage("user", "대한민국의 수도는?"))
                .build();

        OpenAiResponse response = testClient.getChat(request);

        System.out.println(response.output().getFirst().content().getFirst().text());
        assertThat(response).isNotNull();
        assertThat(response.output()).isNotEmpty();
    }
}