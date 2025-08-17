package com.barlow.app.batch.summarization.common;
import com.barlow.client.ai.openai.api.common.OpenAiModel;
import com.barlow.client.ai.openai.api.request.ChatRequest;
import com.barlow.client.ai.openai.api.request.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SummarizationPromptConfig {

    @Value("${prompt.summarization.model}")
    private String model;

    @Value("${prompt.summarization.role}")
    private String role;

    @Value("${prompt.summarization.message}")
    private String message;

    @Bean
    public ChatRequest.Builder summarizeChatBuilder() {
        return ChatRequest.builder()
                .model(OpenAiModel.findByValue(model))
                .addMessage(ChatMessage.ofDeveloper(message));
    }
}
