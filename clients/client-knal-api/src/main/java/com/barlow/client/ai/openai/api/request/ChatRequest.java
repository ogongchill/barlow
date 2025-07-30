package com.barlow.client.ai.openai.api.request;

import com.barlow.client.ai.openai.api.common.OpenAiModel;

import java.util.ArrayList;
import java.util.List;

public class ChatRequest {

    private OpenAiModel model;
    private List<ChatMessage> input;

    public ChatRequest(OpenAiModel model, List<ChatMessage> messages) {
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
        private List<ChatMessage> messages = new ArrayList<>();

        private Builder() {
        }

        private Builder(OpenAiModel model, List<ChatMessage> messages) {
            this.model = model;
            this.messages = new ArrayList<>(messages);
        }

        public Builder model(OpenAiModel model) {
            this.model = model;
            return this;
        }

        public Builder messages(List<ChatMessage> messages) {
            this.messages = messages;
            return this;
        }

        public Builder addMessage(ChatMessage message) {
            messages.add(message);
            return this;
        }

        public ChatRequest build() {
            return new ChatRequest(model, messages);
        }

        public Builder copy() {
            return new Builder(this.model, List.copyOf(this.messages));
        }
    }
}

