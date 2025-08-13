package com.barlow.client.ai.openai.api;


import com.barlow.client.ai.openai.api.common.OpenAiModel;
import com.barlow.client.ai.openai.api.common.OpenAiResponseStatus;
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
    void testBackgroundFalseRequest() {
        ChatRequest request = ChatRequest.builder()
                .model(OpenAiModel.GPT_4_1_MINI)
                .addMessage(new ChatMessage("user", "대한민국의 수도는?"))
                .background(false)
                .build();

        OpenAiResponse response = testClient.getChat(request);

        System.out.println(response.output().getFirst().content().getFirst().text());
        assertThat(response).isNotNull();
        assertThat(response.output()).isNotEmpty();
    }

    @Test
    void testBackgroundTrueRequest() {
        ChatRequest request = ChatRequest.builder()
                .model(OpenAiModel.GPT_4_1_MINI)
                .addMessage(new ChatMessage("user", "대한민국의 수도는?"))
                .background(true)
                .build();

        OpenAiResponse response = testClient.getChat(request);

        System.out.println(response);
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(OpenAiResponseStatus.QUEUED);
    }

    @Test
    void testGetByResponseId() {
        ChatRequest request = ChatRequest.builder()
                .model(OpenAiModel.GPT_4_1_MINI)
                .addMessage(new ChatMessage("user", "대한민국의 수도는?"))
                .background(true)
                .build();

        OpenAiResponse background = testClient.getChat(request);
        OpenAiResponse retrieveById = testClient.getChat(background.id());
        System.out.println(retrieveById);
        assertThat(retrieveById).isNotNull();
    }
}