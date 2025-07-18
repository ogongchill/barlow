package com.barlow.client.ai.openai.api.request;

import com.barlow.client.ai.openai.api.common.OpenAiModel;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequest {

    private OpenAiModel model;
    private List<ChatMessage> input;

    public ChatCompletionRequest(OpenAiModel model, List<ChatMessage> messages) {
        this.model = model;
        this.input = messages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getModel() {
        return model.getValue();
    }

    public List<ChatMessage> getInput() {
        return input;
    }

    public static class Builder {

        private OpenAiModel model;
        private final List<ChatMessage> messages = new ArrayList<>();

        public Builder model(OpenAiModel model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<ChatMessage> messages) {
            this.messages.addAll(messages);
            return this;
        }

        public Builder addMessage(ChatMessage message) {
            messages.add(message);
            return this;
        }

        public ChatCompletionRequest build() {
            return new ChatCompletionRequest(model, messages);
        }
    }
}

